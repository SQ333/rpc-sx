package com.sx.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.sx.config.RegistryConfig;
import com.sx.model.ServiceMetaInfo;
import com.sx.utils.ConfigUtils;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * EtcdRegistry
 *
 * @author SQ
 */
@Slf4j
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * 本地注册节点的 key 集合
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 监听的 key 集合
     */
    private final Set<String> watchKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点目录
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence val = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, val, putOption).get();

        localRegisterNodeKeySet.add(registryKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();

        kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8));

        localRegisterNodeKeySet.remove(registryKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceName) {
        // 优先从缓存中获取
        List<ServiceMetaInfo> cacheServiceMetaInfoList = registryServiceCache.readCache();
        if (cacheServiceMetaInfoList != null) {
            return cacheServiceMetaInfoList;
        }

        // 拼接查询前缀
        String searchPrefix = ETCD_ROOT_PATH + serviceName + "/";

        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                    getOption
            ).get().getKvs();

            // 解析服务列表
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream().map(
                    keyValue -> {
                        // 监听 key
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);

                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());

            // 写入缓存
            registryServiceCache.writeCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("get service list failed ", e);
        }
    }

    @Override
    public void destroy() {
        log.info("current node is down, cancel all lease");

        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                log.error("delete key failed, key: {}", key);
                throw new RuntimeException("delete key failed, key: " + key);
            }
        }

        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/25 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValueList = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get().getKvs();

                        // 节点已过期 需要重启节点
                        if (CollUtil.isEmpty(keyValueList)) {
                            continue;
                        }
                        // 节点未过期 重新注册
                        KeyValue keyValue = keyValueList.get(0);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(
                                keyValue.getValue().toString(StandardCharsets.UTF_8),
                                ServiceMetaInfo.class
                        );
                        register(serviceMetaInfo);

                    } catch (Exception e) {
                        log.error("heartbeat check failed, key: {}", key);
                        throw new RuntimeException("heartbeat check failed, key: " + key);
                    }
                }
            }
        });

        // 启动定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {

        Watch watchClient = client.getWatchClient();

        boolean newWatch = watchKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        case PUT:
                            log.info("watch put event: {}", event);
                            break;
                        case DELETE:
                            log.info("watch delete event: {}", event);
                            registryServiceCache.clearCache();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}

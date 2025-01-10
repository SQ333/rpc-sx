package com.sx.registry;

import com.sx.config.RegistryConfig;
import com.sx.model.ServiceMetaInfo;

import java.util.List;

/**
 * Registry
 * TODO zookeeper
 *
 * @author SQ
 */
public interface Registry {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceName);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测 服务端
     */
    void heartBeat();

    /**
     * 服务监听 消费端
     */
    void watch(String serviceNodeKey);
}

package com.sx;

import com.sx.config.RegistryConfig;
import com.sx.config.RpcConfig;
import com.sx.constant.RpcConstant;
import com.sx.registry.Registry;
import com.sx.registry.RegistryFactory;
import com.sx.serializer.Serializer;
import com.sx.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

/**
 * RpcApplication
 * RPC 框架启动类
 * 相当于 holder，存放全局使用的变量，双锁单例模式的实现
 *
 * @author SQ
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化 RpcConfig
     */
    public static void init(RpcConfig rpcConfig) {
        RpcApplication.rpcConfig = rpcConfig;
        log.info("rpc init success, config: [{}]", rpcConfig);

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init success, config: [{}]", registryConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 初始化 RpcConfig 从配置文件
     */
    public static void init() {
        RpcConfig rpcConfig;
        try {
            rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置文件加载失败，使用默认配置
            log.error("load config error", e);
            rpcConfig = new RpcConfig();
        }
        init(rpcConfig);
    }

    /**
     * 获取 RpcConfig
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }

    public static void main(String[] args) {
        Serializer serializer = null;
        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
        for (Serializer service : serviceLoader) {
            serializer = service;
        }
        System.out.println(serializer);
    }
}

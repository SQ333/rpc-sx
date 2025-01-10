package com.sx.bootstrap;

import com.sx.RpcApplication;
import com.sx.config.RegistryConfig;
import com.sx.config.RpcConfig;
import com.sx.model.ServiceMetaInfo;
import com.sx.model.ServiceRegisterInfo;
import com.sx.registry.LocalRegistry;
import com.sx.registry.Registry;
import com.sx.registry.RegistryFactory;
import com.sx.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * ProviderBootstrap
 *
 * @author SQ
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // RPC 框架初始化
        RpcApplication.init();
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            // 注册服务
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.registry(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register service: " + serviceName, e);
            }

        }
        // 启动 TCP 服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());

//        // 启动 HTTP 服务器
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStart(rpcConfig.getServerPort());
    }
}

package com.sx.example.provider;

import com.sx.RpcApplication;
import com.sx.bootstrap.ProviderBootstrap;
import com.sx.config.RegistryConfig;
import com.sx.config.RpcConfig;
import com.sx.example.common.service.UserService;
import com.sx.model.ServiceMetaInfo;
import com.sx.model.ServiceRegisterInfo;
import com.sx.registry.LocalRegistry;
import com.sx.registry.Registry;
import com.sx.registry.RegistryFactory;
import com.sx.server.HttpServer;
import com.sx.server.VertxHttpServer;
import com.sx.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * ProviderExample
 *
 * @author SQ
 */
public class ProviderExample {

    public static void main(String[] args) {
        // 注册服务集合
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 启动服务
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}

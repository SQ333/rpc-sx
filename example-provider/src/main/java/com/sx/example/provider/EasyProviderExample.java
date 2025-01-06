package com.sx.example.provider;

import com.sx.example.common.service.UserService;
import com.sx.sxrpc.registry.LocalRegistry;
import com.sx.sxrpc.server.HttpServer;
import com.sx.sxrpc.server.VertxHeepServer;

public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.registry(UserService.class.getName(), UserServiceImpl.class);

        // 提供服务
        HttpServer httpServer = new VertxHeepServer();
        httpServer.doStart(2828);
    }
}

package com.sx.example.provider;

import com.sx.RpcApplication;
import com.sx.config.RpcConfig;

public class EasyProviderExample {

    public static void main(String[] args) {
        // RPC 初始化
        RpcApplication.init();

        System.out.println("EasyProviderExample");
        System.out.println(RpcApplication.getRpcConfig());
    }
}

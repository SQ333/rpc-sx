package com.sx.example.consumer;

import com.sx.example.common.model.User;
import com.sx.example.common.service.UserService;
import com.sx.sxrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {

    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("sx");

        // 调用getUser方法
        User result = userService.getUser(user);
        if (result != null) {
            System.out.println("result: " + result.getName());
        } else {
            System.out.println("result is null");
        }
    }
}

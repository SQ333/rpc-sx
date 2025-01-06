package com.sx.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sx.example.common.model.User;
import com.sx.example.common.service.UserService;
import com.sx.sxrpc.model.RpcRequest;
import com.sx.sxrpc.model.RpcResponse;
import com.sx.sxrpc.serializer.JdkSerializer;
import com.sx.sxrpc.serializer.Serializer;

/**
 * 代理类
 */
public class UserServiceProxy implements UserService {
    /**
     * 获取用户
     *
     * @param user 用户
     * @return 用户
     */
    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();

        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;

            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:2828/")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

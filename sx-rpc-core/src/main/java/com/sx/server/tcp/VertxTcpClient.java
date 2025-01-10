package com.sx.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.sx.RpcApplication;
import com.sx.model.RpcRequest;
import com.sx.model.RpcResponse;
import com.sx.model.ServiceMetaInfo;
import com.sx.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * VertxTcpClient
 *
 * @author SQ
 */
@Slf4j
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        // tcp 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        // 同步处理响应
        CompletableFuture<Object> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        log.info("Connected to server");
                        NetSocket socket = result.result();

                        // 构造消息 发送请求
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerialization((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);

                        // 编码
                        try {
                            Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encode);
                        } catch (IOException e) {
                            log.error("Encode message error", e);
                            throw new RuntimeException(e);
                        }

                        // 接收响应
                        TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                            try {
                                ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                // 完成响应
                                responseFuture.complete(decode.getBody());
                            } catch (IOException e) {
                                log.error("Decode message error", e);
                                throw new RuntimeException(e);
                            }
                        });
                        socket.handler(bufferHandlerWrapper);
                    } else {
                        log.error("Failed to connect to server");
                    }
                });
        // 等待响应
        RpcResponse rpcResponse = (RpcResponse) responseFuture.get();
        netClient.close();
        return rpcResponse;
    }

    public void start() {
        // 创建 Vertx 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 客户端
        vertx.createNetClient().connect(7676, "localhost", result -> {

            if (result.succeeded()) {
                log.info("Connected to server");

                NetSocket socket = result.result();
                // 发送数据
                for (int i = 0; i < 1000; i ++ ) {
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, server!Hello, server!Hello, server!Hello, server!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendBytes(str.getBytes());
                    log.info(String.valueOf(str.getBytes().length));
                    socket.write(buffer);
                }
                // 处理响应
                socket.handler(buffer -> {
                    log.info("Received data: {}", buffer.toString());
                });
            } else {
                log.error("Failed to connect to server: ", result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}

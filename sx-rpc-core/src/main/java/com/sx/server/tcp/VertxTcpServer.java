package com.sx.server.tcp;

import com.sx.protocol.ProtocolConstant;
import com.sx.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.slf4j.Slf4j;

/**
 * VertxTcpServer
 *
 * @author SQ
 */
@Slf4j
public class VertxTcpServer implements HttpServer {


    @Override
    public void doStart(int port) {
        // 创建 Vertx 实例
        Vertx vertx = Vertx.vertx();

        // 创建 TCP 服务
        NetServer netServer = vertx.createNetServer();

        // 处理请求
        netServer.connectHandler(new TcpServerHandler());
//        netServer.connectHandler(socket -> {
//            RecordParser parser = RecordParser.newFixed(8);
//            parser.setOutput(new Handler<Buffer>() {
//                int size = -1;
//                Buffer resultBuffer = Buffer.buffer();
//
//                @Override
//                public void handle(Buffer buffer) {
//                    if (size == -1) {
//                        size = buffer.getInt(4);
//                        log.info("Received size: {}", size);
//                        parser.fixedSizeMode(size);
//                        resultBuffer.appendBuffer(buffer);
//                    } else {
//                        resultBuffer.appendBuffer(buffer);
//                        log.info("Received data: {}", resultBuffer.toString());
//                        parser.fixedSizeMode(8);
//                        size = -1;
//                        resultBuffer = Buffer.buffer();
//                    }
//                }
//            });
//            socket.handler(parser);
//        });

        // 监听端口
        netServer.listen(port, result -> {
            if (result.succeeded()) {
                log.info("Server started on port: {}", port);
            } else {
                log.error("TCP Server start failed: ", result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(7676);
    }
}

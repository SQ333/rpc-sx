package com.sx.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProtocolMessage
 *
 * @author SQ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    @Data
    /**
     * 协议头
     */
    public static class Header {
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本
         */
        private byte version;

        /**
         * 序列化
         */
        private byte serialization;

        /**
         * 消息类型 (请求/响应)
         */
        private byte type;

        /**
         * 状态
         */
        private byte status;

        /**
         * 请求ID
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}

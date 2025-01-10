package com.sx.protocol;

/**
 * ProtocolConstant
 *
 * @author SQ
 */
public interface ProtocolConstant {
    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    byte PROTOCOL_MAGIC = (byte) 0x01;

    /**
     * 协议版本
     */
    byte PROTOCOL_VERSION = (byte) 0x01;
}

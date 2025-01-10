package com.sx.protocol;

import com.sx.model.RpcRequest;
import com.sx.model.RpcResponse;
import com.sx.serializer.Serializer;
import com.sx.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * ProtocolMessageDecoder
 *
 * @author SQ
 */
@Slf4j
public class ProtocolMessageDecoder {

    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            log.error("Unsupported protocol magic: {}", magic);
            throw new RuntimeException("Unsupported protocol magic: " + magic);
        }

        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerialization(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        byte[] bodyBytes = buffer.getBytes(17, 17 + header.getBodyLength());
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getEnumByKey(header.getSerialization());

        if (serializerEnum == null) {
            log.error("Unsupported serialization: {}", header.getSerialization());
            throw new RuntimeException("Unsupported serialization: " + header.getSerialization());
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        ProtocolMessageTypeEnum messageType = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageType == null) {
            log.error("Unsupported message type: {}", header.getType());
            throw new RuntimeException("Unsupported message type: " + header.getType());
        }

        switch (messageType) {
            case REQUEST:
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHER:
            default:
                log.error("Unsupported message type: {}", header.getType());
                throw new RuntimeException("Unsupported message type: " + header.getType());
        }
    }
}

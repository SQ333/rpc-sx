package com.sx.protocol;

import cn.hutool.core.util.IdUtil;
import com.sx.model.RpcConstant;
import com.sx.model.RpcRequest;
import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * ProtocolMessageTest
 *
 * @author SQ
 */
public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws IOException {
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerialization((byte) ProtocolMessageSerializerEnum.HESSIAN.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("sx");
        rpcRequest.setMethodName("test");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"test"});

        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);
        Assertions.assertNotNull(decode);
    }
}

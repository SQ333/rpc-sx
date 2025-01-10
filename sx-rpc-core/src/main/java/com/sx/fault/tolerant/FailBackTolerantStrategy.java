package com.sx.fault.tolerant;

import com.sx.model.RpcResponse;

import java.util.Map;

/**
 * FailBackTolerantStrategy
 *
 * @author SQ
 */
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        return null;
    }
}

package com.sx.fault.tolerant;

import com.sx.model.RpcResponse;

import java.util.Map;

/**
 * FailOverTolerantStrategy
 *
 * @author SQ
 */
public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        return null;
    }
}

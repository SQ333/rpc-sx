package com.sx.fault.tolerant;

import com.sx.model.RpcResponse;

import java.util.Map;

/**
 * TolerantStratery
 *
 * @author SQ
 */
public interface TolerantStrategy {

    /**
     * Tolerant
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception;
}

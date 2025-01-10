package com.sx.fault.tolerant;

import com.sx.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * FailSafeTolerantStrategy
 *
 * @author SQ
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy {
    /**
     * 静默失败
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        log.error("FailSafe Tolerant Strategy", e);
        return new RpcResponse();
    }
}

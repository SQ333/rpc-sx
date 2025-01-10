package com.sx.fault.tolerant;

import com.sx.model.RpcResponse;

import java.util.Map;

/**
 * FailFastTolerantStratrgy
 *
 * @author SQ
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    /**
     * 快速失败
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        throw new RuntimeException("FailFast Tolerant Strategy", e);
    }
}

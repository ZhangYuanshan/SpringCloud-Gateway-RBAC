package com.imlehr.quizhub.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author Lehr
 * @create: 2020-03-25
 */
public class FlowLimitHandler {

    public static String blockHotspot(String p1, String p2, BlockException e)
    {
        return "......Hot Fail！";
    }
}

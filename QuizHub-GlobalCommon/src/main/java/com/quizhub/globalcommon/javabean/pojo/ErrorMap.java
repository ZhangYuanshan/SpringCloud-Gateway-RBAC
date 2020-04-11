package com.quizhub.globalcommon.javabean.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-03-29
 */
public class ErrorMap<K, V> extends HashMap {

    private ResponseMsg wrappedResp;

    public ErrorMap(Map<String, Object> body)
    {
        wrappedResp = ResponseMsg.ofFail(body.get("status").toString(),body.get("message").toString());
    }

    public ResponseMsg getResponseMsg()
    {
        return wrappedResp;
    }



}

package com.quizhub.common.javabean;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-02-03
 */


@Getter
public class RpcResponse{

    private Boolean success;

    private String errMsg;

    private String retCode;

    private Map entity;

    private List<Map> list;

    /**
     * 主要是给data做包装
     * @param result
     */
    public RpcResponse(ResponseMsg result)
    {
        success = result.getSuccess();
        errMsg = result.getErrMsg();
        retCode = result.getRetCode();

        Object resultData = result.getData();
        if(resultData!=null)
        {
            if(resultData instanceof Map)
            {
                entity = (Map) result.getData();
            }
            if(resultData instanceof List)
            {
                list = (List) result.getData();
            }
        }
        else
        {
            entity = Collections.emptyMap();
            list = Collections.emptyList();
        }

    }


    public String getString(String name)
    {
        return entity.get(name).toString();
    }

    public Integer getInteger(String name)
    {
        return Integer.valueOf(entity.get(name).toString());
    }

    public Boolean getBoolean(String name)
    {
        return Boolean.valueOf(entity.get(name).toString());
    }

    public List getList()
    {
        return list;
    }

    public Map getEntity()
    {
        return entity;
    }






}

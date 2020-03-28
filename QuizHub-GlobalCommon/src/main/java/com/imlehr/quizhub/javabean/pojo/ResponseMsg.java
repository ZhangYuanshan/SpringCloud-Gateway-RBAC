package com.imlehr.quizhub.javabean.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lehr
 * @create: 2020-02-03
 */


@Setter
@Getter
public class ResponseMsg<T> {

    private Boolean success;

    private String errMsg;

    private String errCode;

    private T data;


    private ResponseMsg(Boolean success,T data, String errMsg, String errCode) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.data = data;
    }


    public static ResponseMsg ofSuccess(Object data)
    {
        return new ResponseMsg(true,data,null,null);
    }


    public static ResponseMsg ofSuccess()
    {
        return new ResponseMsg(true,null,null,null);
    }

    public static ResponseMsg ofFail(MyException e)
    {
        return new ResponseMsg(false,null,e.getMessage(),e.getErrorCode());
    }


}

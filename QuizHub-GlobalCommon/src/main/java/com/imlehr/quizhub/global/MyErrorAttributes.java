package com.imlehr.quizhub.global;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author lehr
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {




    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                  boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new HashMap<>(4);
        errorAttributes.put("timestamp", "fuck you");


        return errorAttributes;
    }


}

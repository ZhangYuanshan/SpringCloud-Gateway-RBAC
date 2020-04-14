package com.quizhub.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lehr
 * @create: 2020-04-13
 */
public class HeaderUtils {

        public static String getVisitorId(HttpServletRequest request)
        {
            //todo 这里是mock的
            return "Lehr";//;request.getHeader("userId");
        }

}


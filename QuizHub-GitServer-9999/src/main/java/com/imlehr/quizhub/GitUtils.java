package com.imlehr.quizhub;

import lombok.Builder;

/**
 * @author Lehr
 * @create: 2020-04-05
 */
@Builder
public class GitUtils {

    private final String user = "Lehr";
    private final String gitRoot = "/home/lehr/GitRepo/Lehr130";
    private final String exportAll=" ";
    private String pathInfo;
    private String requestUrl;
    private String service;
    private String requestMethod;
    private final String cgiVersion = "CGI/1.1";
    private final String protocal = "HTTP/1.0";
    private final String maxBuffer = "10M";
    private final String software = "Java";
    private String contentType;
    private String serverName;
    private String httpHost;
    private final String serverPort = "9999";
    private String cl;


    public String[] env()
    {
        String[] env = new String[18];
        env[0] = "GIT_PROJECT_ROOT=" + gitRoot;
        env[1] = "GIT_HTTP_EXPORT_ALL=" + exportAll;
        env[2] = "REMOTE_USER=" + user;
        env[3] = "GIT_HTTP_MAX_REQUEST_BUFFER=" + maxBuffer;
        env[4] = "SERVER_SOFTWARE="+software;
        env[5] = "SERVER_NAME=";
        env[13] = "CONTENT_TYPE="+contentType;
        env[6] = "SERVER_PROTOCOL="+protocal;
        env[7] = "HTTP_HOST=";
        env[8] = "GATEWAY_INTERFACE="+cgiVersion;
        env[9] = "REQUEST_METHOD="+ requestMethod;
        env[10] = "QUERY_STRING=" +service;
        env[11] = "REQUEST_URI="+requestUrl;
        env[12] = "PATH_INFO="+pathInfo;
        env[14] = "SERVER_NAME="+serverName;
        env[15] = "HTTP_HOST="+httpHost;
        env[16] = "SERVER_PORT="+serverPort;
        env[17] = "CONTENT_LENGTH="+cl;

        return env;
    }



}

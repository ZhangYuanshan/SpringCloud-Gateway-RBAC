package com.quizhub.gitserver.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GitTransportService {

    void getRequest(String repo, String owner, String service, HttpServletRequest req, HttpServletResponse res);



    void postRequest(byte[] info, String repo, String owner, String service, HttpServletRequest req, HttpServletResponse res);



}

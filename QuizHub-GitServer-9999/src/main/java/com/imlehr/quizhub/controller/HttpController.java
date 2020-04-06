package com.imlehr.quizhub.controller;

import com.imlehr.quizhub.GitUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Lehr
 * @create: 2020-03-30
 */
@RestController
@Slf4j
public class HttpController {

    private static final String gitRoot = "/home/lehr/GitRepo/Lehr130";

    @SneakyThrows
    @GetMapping(value = "**")
    private void gitcgi(String service, HttpServletRequest req, HttpServletResponse res) {

        System.out.println("in get!!!!!!!");

        res.setStatus(404);
//
//
//        String[] env = GitUtils.builder()
//                .contentType(req.getContentType())
//                .pathInfo(req.getRequestURI())
//                .requestMethod(req.getMethod())
//                .requestUrl(req.getRequestURI())
//                .service("service="+service).cl("")
//                .build().env();
//
//        Process p = Runtime.getRuntime().exec("git http-backend", env);
//
//        InputStream is = p.getInputStream();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//        StringBuilder line = new StringBuilder();
//        String linea;
//
//
//
//        while ((linea = reader.readLine()) != null) {
//
//            if (linea.contains("Expires") || linea.contains("Pragma") || linea.contains("Cache-Con") || linea.contains("Conten") || linea.length() < 2) {
//                if (linea.length() > 5) {
//                    String[] heds = linea.split(": ");
//
//                    if (linea.contains("Expires")) {
//                        res.setHeader("Expries", "Sun, 05 Apr 2022 05:57:02 GMT");
//                    } else {
//                        res.setHeader(heds[0], heds[1]);
//                    }
//                }
//            } else {
//                line.append(linea+"\n");
//            }
//        }
//
//
//
//        String newl = line.toString().substring(0, line.toString().length() - 1);
//
//
//        res.getOutputStream().write(newl.getBytes());

    }





    @SneakyThrows
    @PostMapping(value = "**")
    private void postcgi(@RequestBody byte[] service, HttpServletRequest req, HttpServletResponse res) {





        System.out.println("in post!!!!!!!");
        String[] env = GitUtils.builder()
                .contentType(req.getContentType())
                .pathInfo(req.getRequestURI())
                .requestMethod(req.getMethod())
                .requestUrl(req.getRequestURI())
                .cl(String.valueOf(service.length))
                .build().env();

        Process p = Runtime.getRuntime().exec("git http-backend", env);

        OutputStream outputStream = p.getOutputStream();
        outputStream.write(service);
        outputStream.flush();




        BufferedInputStream is = new BufferedInputStream(p.getInputStream());

        is.skip(165);
        if(req.getRequestURI().contains("upload"))
        {
            res.setHeader("Cache-Control","no-cache, max-age=0, must-revalidate");
            res.setHeader("Content-Type","application/x-git-upload-pack-result");
        }
        else
        {
            res.setHeader("Content-Type","application/x-git-recive-pack-result");
        }
        //todo
        is.transferTo(res.getOutputStream());

    }


    @SneakyThrows
    public String dumb(String path) {


        File file = new File("/home/lehr/GitRepo/Lehr130" + path);
        System.out.println(file.getAbsolutePath());
        Long len = file.length();
        byte[] fileContent = new byte[len.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(fileContent);
        in.close();

        return new String(fileContent);

    }


}

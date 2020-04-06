package com.imlehr.quizhub.service;

import com.imlehr.quizhub.GitUtils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Lehr
 * @create: 2020-04-05
 */
public class mock {

    @SneakyThrows
    public static void main(String[] args) {

        String path = "/Lehr.git/git-upload-pack";
        String contentType = "application/x-git-upload-pack-request";
        String gitRoot = "/home/lehr/GitRepo/Lehr130";
        String requestMethod = "POST";

        String s = "0098want 09ba80fc599b381d15762dd0eac2d752991207e0 multi_ack_detailed no-done side-band-64k thin-pack ofs-delta deepen-since deepen-not agent=git/2.17.1\n" +
                "00000009done\n";
        String pi = String.valueOf(s.length());







        System.out.println("----------↓----------");


        String[] env = GitUtils.builder()
                .contentType(contentType)

                .pathInfo(path)
                .requestMethod(requestMethod)
                .requestUrl(path)
                .service(" ").serverName("")
                .httpHost(" ").cl(pi)
                .build().env();

        Process p = Runtime.getRuntime().exec("git http-backend", env);
        OutputStream outputStream = p.getOutputStream();
        outputStream.write(s.getBytes());
        outputStream.flush();
        InputStream is = p.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String linea;

        while ((linea = reader.readLine()) != null) {
            System.out.println(linea);
        }

        System.out.println("------------↑------------");

    }


}

package com.quizhub.elasticsearch.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * @author Lehr
 * @create: 2020-04-13
 */
@Component
public class ESConfig {

    //主要就是这个api
    @Bean
    public TransportClient getTransportClient()
    {
        TransportClient client = null;
        try
        {
            Settings settings = Settings.builder()
                .put("cluster.name","lehrs")
                    .put("node.name","test")
                    //自动嗅探新es节点加入
                    .put("client.transport.sniff", true)
                    .put("thread_pool.search.size", 5).build();

            client = new PreBuiltTransportClient(settings);

            //配置es连接信息
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("127.0.0.1"),9300);
            client.addTransportAddress(transportAddress);

        }catch (Exception e)
        {

        }

        return  client;

    }

}

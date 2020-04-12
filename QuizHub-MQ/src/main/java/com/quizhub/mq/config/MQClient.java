package com.quizhub.mq.config;

import com.quizhub.mq.javabean.dto.RepoRecordDTO;
import com.quizhub.mq.javabean.enums.MqTags;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQClient {

    private static String producerGroup = "QuizHub";
    private static DefaultMQProducer producer;

    static {
        //示例生产者
        producer = new DefaultMQProducer(producerGroup);
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);
        //绑定name server
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        start();
    }

    /**
     * 对象在使用之前必须要调用一次，只能初始化一次
     */
    private static void start() {
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * 目前默认是one way的
     *
     * @param message
     */
    public static void sendMessage(MqTags tags, String message) {

        Message msg = new Message(JmsConfig.TOPIC, tags.name(), message.getBytes());
        try {
            producer.sendOneway(msg);
        } catch (Exception e) {
            log.warn("消息发送出错，忽略...");
        }
    }

    public static void sendMessage(RepoRecordDTO record) {

        Message msg = new Message(JmsConfig.TOPIC, MqTags.RECORD.name(), record.toString().getBytes());
        try {
            producer.sendOneway(msg);
        } catch (Exception e) {
            log.warn("消息发送出错，忽略...");
        }
    }

    /**
     * 一般在应用上下文，使用上下文监听器，进行关闭
     */
    public static void shutdown() {
        producer.shutdown();
    }
}
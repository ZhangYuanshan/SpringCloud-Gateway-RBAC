package com.quizhub.logger.service.impl;

import com.github.xiaoymin.knife4j.spring.util.ByteUtils;
import com.quizhub.logger.service.ActivityService;
import com.quizhub.mq.javabean.dto.RepoRecordDTO;
import com.quizhub.mq.javabean.enums.MqTags;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {


    @Override
    public void recordActivities(List<MessageExt> messages) throws UnsupportedEncodingException {
        for (Message msg : messages) {
            String topic = msg.getTopic();
            String tags = msg.getTags();

            {
                String body = new String(msg.getBody(), "utf-8");

                System.out.println("------------------------\n" +
                        "topic:" + topic + "\n" +
                        "tags:" + tags + "\n" +
                        "msg:" + body);
            }
        }
    }
}

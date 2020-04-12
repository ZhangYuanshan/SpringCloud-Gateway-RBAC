package com.quizhub.logger.service;

import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author Lehr
 * @create: 2020-04-12
 */
public interface ActivityService {

    void recordActivities(List<MessageExt> messages) throws UnsupportedEncodingException;

}

package com.alibaba.otter.node.etl.load.loader.mq;

import com.alibaba.otter.shared.common.model.config.data.mq.RocketMqMediaSource;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;

public class RocketMqSenderTest {

    @Test
    public void send() throws MQClientException {
        RocketMqMediaSource rocketMqMediaSource = new RocketMqMediaSource();
        rocketMqMediaSource.setGroupName("OTTER_CUSTOM_TEST");
        rocketMqMediaSource.setNamesrvAddr("10.111.27.2:9876");
        RocketMqSender rocketMQSender = new RocketMqSender(rocketMqMediaSource);
        rocketMQSender.start();
    }
}
package com.alibaba.otter.node.etl.load.loader.mq;

import com.alibaba.otter.shared.common.model.config.data.mq.RocketMqMediaSource;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class RocketMqSender {

    private RocketMqMediaSource rocketMqMediaSource;

    private DefaultMQProducer producer;

    RocketMqSender(RocketMqMediaSource rocketMqMediaSource) {
        assert rocketMqMediaSource != null;
        this.rocketMqMediaSource = rocketMqMediaSource;
        String groupName = rocketMqMediaSource.getGroupName();
        String namesrvAddr = rocketMqMediaSource.getNamesrvAddr();
        this.producer = new DefaultMQProducer(groupName);
        this.producer.setNamesrvAddr(namesrvAddr);
    }

    public void start() throws MQClientException {
        this.producer.start();
        this.producer.setRetryTimesWhenSendAsyncFailed(0);

        String createTopicKey = this.producer.getCreateTopicKey();
        this.producer.createTopic(createTopicKey, "OTTER_CUSTOM_TEST_TOPIC", this.producer.getDefaultTopicQueueNums());
    }

    public void send(String topic, String tag, String key, String message) {
        try {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(topic,
                    tag,
                    key,
                    message.getBytes(RemotingHelper.DEFAULT_CHARSET));
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        this.producer.shutdown();
    }

    public static void main(String[] args) {

        RocketMqMediaSource rocketMqMediaSource = new RocketMqMediaSource();
        rocketMqMediaSource.setGroupName("OTTER_CUSTOM_TEST");
        rocketMqMediaSource.setNamesrvAddr("localhost:9876");
        RocketMqSender rocketMQSender = new RocketMqSender(rocketMqMediaSource);
        try {
            rocketMQSender.start();
        } catch (MQClientException e) {
            rocketMQSender.shutdown();
        }
        rocketMQSender.send("OTTER_CUSTOM_TEST_TOPIC", "OTTER1", "KEY1", "TEST");
        rocketMQSender.shutdown();
    }
}

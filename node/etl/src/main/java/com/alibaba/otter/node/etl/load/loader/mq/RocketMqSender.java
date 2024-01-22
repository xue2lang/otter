package com.alibaba.otter.node.etl.load.loader.mq;

import com.alibaba.otter.shared.common.model.config.data.mq.RocketMqMediaSource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class RocketMqSender {

    private RocketMqMediaSource rocketMqMediaSource;

    private DefaultMQProducer producer;

    public RocketMqSender(RocketMqMediaSource rocketMqMediaSource) {
        assert rocketMqMediaSource != null;
        this.rocketMqMediaSource = rocketMqMediaSource;
        String groupName = rocketMqMediaSource.getGroupName();
        String namesrvAddr = rocketMqMediaSource.getNamesrvAddr();
        this.producer = new DefaultMQProducer(groupName);
        this.producer.setInstanceName(groupName);
        this.producer.setNamesrvAddr(namesrvAddr);
    }

    public void start() throws MQClientException {
        this.producer.start();
        this.producer.setRetryTimesWhenSendAsyncFailed(0);
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

    public static void main(String[] args) throws MQClientException {

        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_group_name5");

        // Specify name server addresses.
        consumer.setNamesrvAddr("localhost:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("test_topic1", "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages", Thread.currentThread().getName());
                for(MessageExt messageExt : msgs){
                    String s = new String(messageExt.getBody(), Charset.forName("UTF-8"));
                    System.out.println(s);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}

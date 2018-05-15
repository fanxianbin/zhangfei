package com.rocketmq.transaction;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Consumer
{
    public static void main(String[] args) throws MQClientException
    {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("conGroup");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.subscribe("order","*");
        consumer.registerMessageListener(new MessageListenerConcurrently()
        {
            
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context)
            {
                System.out.println("收到的消息"+list);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("开始消费");
    }
}

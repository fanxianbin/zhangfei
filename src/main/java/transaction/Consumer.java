package transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageQueueListener;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class Consumer
{
    public static void main(String[] args) throws MQClientException, IOException
    {
        final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("conGroup");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.subscribe("order","*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        //consumer.subscribe("order",MessageSelector.bySql("a = between 0 and 3"));
        //consumer.subscribe("order","transaction.OrderMessageFilter",MixAll.file2String("C:\\softwares\\develop\\workspace\\efg\\src\\main\\java\\transaction\\OrderMessageFilter.java"));
        //consumer.subscribe("orders","b");
        //consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setMaxReconsumeTimes(2);
        consumer.setConsumeThreadMax(20);
        consumer.setConsumeThreadMin(20);
        consumer.registerMessageListener(new MessageListenerConcurrently()
        {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context)
            {
                System.out.println("收到的消息"+list);
                System.out.println(list.get(0).getClass());
                try
                {
                    Thread.sleep(0);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                MessageExt m = list.get(0);
                System.out.println("xxxxxxx"+consumer.getMaxReconsumeTimes());
                consumer.setMaxReconsumeTimes(consumer.getMaxReconsumeTimes()+1);
                m.setReconsumeTimes(12);
                if(true){
                    context.setDelayLevelWhenNextConsume(2);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }else{
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }

        });
        consumer.start();
        System.out.println(consumer.getMessageModel());
        System.out.println(consumer);
        System.out.println(consumer.buildMQClientId());
        System.out.println("开始消费");
    }
}

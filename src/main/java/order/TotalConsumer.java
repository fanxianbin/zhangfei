package order;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


public class TotalConsumer
{
    public static void main(String[] args) throws MQClientException
    {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("total_order4");
        consumer.setNamesrvAddr("localhost:9876");
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe("orders","*");
        consumer.registerMessageListener(new MessageListenerOrderly(){

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list,
                    ConsumeOrderlyContext context)
            {
                context.setAutoCommit(true);
                MessageExt message = list.get(0);
                System.out.println(message.toString());
                context.setSuspendCurrentQueueTimeMillis(1000);
                try
                {
                    Thread.sleep(500000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.out.println(consumer.buildMQClientId());
        System.out.println("开始监听");
    }
}

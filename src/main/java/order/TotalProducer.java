package order;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class TotalProducer
{
    public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException
    {
        DefaultMQProducer producer = new DefaultMQProducer("total_order");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Map<String,String> params = new HashMap<String,String>();
        params.put("test_data","123");
        for(int i=0;i<10;i++){
            final int j = i;
            Message m = new Message("orders","tag","order_id_"+i,String.valueOf(System.currentTimeMillis()).getBytes());
            producer.send(m,new MessageQueueSelector()
            {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object obj)
                {
                    System.out.println("-----------------");
                    System.out.println(obj);
                    System.out.println(list);
                    int total = list.size();
                    return list.get(j%total);
                }
            },params);
            Thread.sleep(2);
        }
        producer.shutdown();
        System.out.println("发送完成");
    }
}

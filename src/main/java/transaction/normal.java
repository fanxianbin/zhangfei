package transaction;

import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class normal
{
    public static void main(String[] args) throws MQClientException, RemotingException, MQBrokerException, InterruptedException
    {
        DefaultMQProducer prod = new DefaultMQProducer("conGroup");
        prod.setNamesrvAddr("localhost:9876");
        prod.start();
        String aString = "";
        for(int j=0;j<400;j++){
            aString+="0";
        }
        Message m = new Message("order","a","xaa1123456789",aString.getBytes());
        //m.setDelayTimeLevel(19);
//        MessageAccessor.putProperty(m, "TRAN_MSG", "true");
//        MessageAccessor.putProperty(m, "PGROUP", prod.getProducerGroup());
//        SendResult [sendStatus=SEND_OK, msgId=A9FE89BD4600659E0BFD958FCAB10000, offsetMsgId=A9FE89BD00002A9F00000000004C36D7, messageQueue=MessageQueue [topic=order, brokerName=LAPTOP-TPSVOH6V, queueId=3], queueOffset=0]
//                DefaultRegion
//                null
        SendResult result = prod.send(m);
        System.out.println(result.toString());
        System.out.println(result.getRegionId());
        System.out.println(result.getTransactionId());
        prod.shutdown();
    }
}

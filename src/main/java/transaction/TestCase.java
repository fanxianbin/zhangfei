package transaction;


import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.admin.TopicOffset;
import org.apache.rocketmq.common.admin.TopicStatsTable;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.body.TopicList;
import org.apache.rocketmq.common.sysflag.MessageSysFlag;
import org.apache.rocketmq.console.service.client.MQAdminExtImpl;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExtImpl;
import org.apache.rocketmq.tools.admin.MQAdminExt;

public class TestCase
{
    public static void main(String[] args) throws Exception
    {
        String nameString = null;
        DefaultMQProducer de = new DefaultMQProducer("conGroup");
        de.setNamesrvAddr("localhost:9876");
        de.start();
        MessageExt xx = de.viewMessage("order", "A9FE89BD00002A9F00000000005EE87A");
        System.out.println(xx);
        QueryResult result =  de.queryMessage("order","xxa1222x312", 68, 0, System.currentTimeMillis());
        List<MessageExt> message = result.getMessageList();
        System.out.println(message.size());
        for(MessageExt me : message){
            System.out.println(me.getClass());
            System.out.println(me.getMsgId()+":"+me.getProperties().get("UNIQ_KEY"));
            System.out.println(me.toString());
//            Messag时tQueueOffset(me.getQueueOffset());
            //sr.setTransactionId(transactionId);
            //prod.getDefaultMQProducerImpl().endTransaction(sr, LocalTransactionState.COMMIT_MESSAGE, null);
        }
        de.shutdown();
    }
    
    
}

package transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PropertyKey;

import org.apache.rocketmq.client.QueryResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageClientExt;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.sysflag.MessageSysFlag;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Producer
{
    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException
    {
        
        TransactionMQProducer producer = new TransactionMQProducer("conGroup2");
        producer.setNamesrvAddr("localhost:9876");
        producer.setTransactionCheckListener(new CheckListener());
        //producer.createTopic("newKey","newTopic",2,4);
        producer.setInstanceName("xxxxx");
        System.out.println(producer.buildMQClientId());
        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        System.out.println(producer.getRetryTimesWhenSendFailed());
        // 队列数
        producer.setCheckRequestHoldMax(2000);
        System.out.println(producer.getCompressMsgBodyOverHowmuch());
        producer.start();
        //System.exit(0);
        //Thread.sleep(5000);
        Map<String,String> map = new HashMap<String,String>();
        map.put("test","fack");
        long time = System.currentTimeMillis();
        for(int i=0;i<1;i++){
            System.out.println("开始");
            String aString = "";
            for(int j=0;j<100;j++){
                aString+="0";
            }
            Message mm = new Message("order", "a","xxa1222x312", aString.getBytes());
            //mm.setDelayTimeLevel(3);
            System.out.println(producer.getClass());
            TransactionSendResult t =  producer.sendMessageInTransaction(
                    mm,
                    new TransactionExecutor(),map);
        }
        //System.out.println(result.getSendStatus());
        System.out.println(producer.buildMQClientId());
        System.out.println(System.currentTimeMillis()-time);
        producer.shutdown();
    }
    
    private static class CheckListener implements TransactionCheckListener{

        @Override
        public LocalTransactionState checkLocalTransactionState(MessageExt messgae)
        {
//            System.out.println("我正在检查");
//            System.out.println(messgae);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        
    }
    
    private static class TransactionExecutor implements LocalTransactionExecuter{

        @Override
        public LocalTransactionState executeLocalTransactionBranch(Message message, Object param)
        {
            System.out.println("我正在处理事务");
            System.out.println(message.getClass());
            System.out.println(message.toString());
            System.out.println(param);
            int i=1;
            try
            {
                Thread.sleep(0);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            if(i==0){
                throw new RuntimeException("Could not find db");
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        
    }
}


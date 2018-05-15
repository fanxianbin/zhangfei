package com.rocketmq.transaction;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.client.producer.TransactionSendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Producer
{
    public static void main(String[] args) throws MQClientException, InterruptedException
    {
        TransactionMQProducer producer = new TransactionMQProducer("transP");
        producer.setNamesrvAddr("localhost:9876");
        producer.setTransactionCheckListener(new CheckListener());
        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        // 队列数
        producer.setCheckRequestHoldMax(2000);
        producer.start();
        //Thread.sleep(5000);
        Map<String,String> map = new HashMap<String,String>();
        map.put("test","fack");
        TransactionSendResult result =  producer.sendMessageInTransaction(
                new Message("order", "1234567890", "出水荷花".getBytes()),
                new TransactionExecutor(),map);
        System.out.println(result.getSendStatus());
        producer.shutdown();
    }
    
    private static class CheckListener implements TransactionCheckListener{

        @Override
        public LocalTransactionState checkLocalTransactionState(MessageExt messgae)
        {
            System.out.println("我正在检查");
            System.out.println(messgae);
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        
    }
    
    private static class TransactionExecutor implements LocalTransactionExecuter{

        @Override
        public LocalTransactionState executeLocalTransactionBranch(Message message, Object param)
        {
            System.out.println("我正在处理事务");
            System.out.println(message);
            System.out.println(param);
            int i=1;
            if(i==0){
                throw new RuntimeException("Could not find db");
            }
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        
    }
}


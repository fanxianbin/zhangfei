package transaction;

import java.io.IOException;
import java.util.Set;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.SubscriptionGroupWrapper;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.common.sysflag.MessageSysFlag;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.apache.rocketmq.tools.admin.MQAdminExt;

import com.google.common.collect.Sets;


public class TTT
{
    public static void main(String[] args) throws MQClientException, IOException
    {
       DefaultMQAdminExt admin = new DefaultMQAdminExt();
       admin.setNamesrvAddr("localhost:9876");
       admin.start();
//       Set<String> consumerGroupSet = Sets.newHashSet();
//       try {
//           ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
//           for (BrokerData brokerData : clusterInfo.getBrokerAddrTable().values()) {
//               System.out.println(brokerData);
//               SubscriptionGroupWrapper subscriptionGroupWrapper = admin.getAllSubscriptionGroup(brokerData.selectBrokerAddr(), 3000L);
//               consumerGroupSet.addAll(subscriptionGroupWrapper.getSubscriptionGroupTable().keySet());
//           }
//       }
//       catch(Exception e){
//           e.printStackTrace();
//       }
//       System.out.println(consumerGroupSet);
       String name = MixAll.file2String("C:\\softwares\\develop\\workspace\\efg\\src\\main\\java\\transaction\\OrderMessageFilter.java");
       System.out.println(name);
       admin.createTopic("myTpic","xasx",4);
       admin.shutdown();
    }
}

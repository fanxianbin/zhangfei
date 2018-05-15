package transaction;

import org.apache.rocketmq.common.filter.FilterContext;
import org.apache.rocketmq.common.filter.MessageFilter;
import org.apache.rocketmq.common.message.MessageExt;

public class OrderMessageFilter implements MessageFilter
{

    @Override
    public boolean match(MessageExt message, FilterContext filtercontext)
    {
        System.out.println(123);
        filtercontext.setConsumerGroup("conGroup11");
        return true;
    }
    
}

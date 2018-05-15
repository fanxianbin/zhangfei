package com.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class OrderTest
{
    
    public static class OrderMapper extends Mapper<LongWritable,Text,OrderBean,NullWritable>{
        
        @Override
        protected void map(LongWritable key, Text value,Context context)
                throws IOException, InterruptedException
        {
            System.out.println("map函数");
            String line = value.toString();
            String[] ss = line.split(" +");
            context.write(new OrderBean(new Text(ss[0]),new DoubleWritable( Double.parseDouble(ss[2]))),NullWritable.get());
        }
    }
    
    
    public static class OrderReducer extends Reducer<OrderBean,NullWritable,OrderBean,NullWritable>{
        
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values,Context context)
                throws IOException, InterruptedException
        {
            System.out.println(222);
            context.write(key,NullWritable.get());
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        Configuration conf = new Configuration();
        //conf.set("hdfs.defaultFS","hdfs://localhost:9000");
        Job job = Job.getInstance(conf);
        
        job.setJarByClass(OrderTest.class);
        
        
        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(OrderReducer.class);
        
        job.setGroupingComparatorClass(GroupComparator.class);
        job.setPartitionerClass(OrderIdPartitioner.class);
        job.setNumReduceTasks(2);
        
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        
//        
//        job.setOutputKeyClass(OrderBean.class);
//        job.setOutputValueClass(NullWritable.class);
        
        
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\fanxianb\\Desktop\\order.txt"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\fanxianb\\Desktop\\order"));
        
        boolean flag = job.waitForCompletion(true);
        System.out.println("是否成功"+flag);
    }
    
    public static class GroupComparator extends WritableComparator{
        
        public GroupComparator(){
            //super(OrderBean.class,true);
            System.out.println("GroupComparator初始化");
        }
        
        @Override
        public int compare(WritableComparable a, WritableComparable b)
        {
            System.out.println("compare");
            OrderBean aa = (OrderBean) a;
            OrderBean bb = (OrderBean) b;
            return aa.compareTo(bb);
        }
    }
    
    public static class OrderIdPartitioner extends Partitioner<OrderBean,NullWritable>{

        @Override
        public int getPartition(OrderBean bean, NullWritable value, int partNum)
        {
            System.out.println("OrderIdPartitioner");
            return Math.abs(bean.getOrder_id().hashCode()%partNum);
        }
        
    }
    
    
    public static class OrderBean implements WritableComparable<OrderBean>{

        private Text order_id;
        private DoubleWritable amount;
        
        public OrderBean(Text order_id, DoubleWritable amount)
        {
            System.out.println("OrderBean初始化");
            set(order_id, amount);
        }
        public void set(Text order_id, DoubleWritable amount){
            this.order_id = order_id;
            this.amount = amount;
        }
        public Text getOrder_id()
        {
            return order_id;
        }
        public void setOrder_id(Text order_id)
        {
            this.order_id = order_id;
        }
        public DoubleWritable getAmount()
        {
            return amount;
        }
        public void setAmount(DoubleWritable amount)
        {
            this.amount = amount;
        }
        @Override
        public void readFields(DataInput in) throws IOException
        {
            this.order_id = new Text(in.readUTF());
            this.amount = new DoubleWritable(in.readDouble());
        }
        @Override
        public void write(DataOutput out) throws IOException
        {
            out.writeUTF(order_id.toString());
            out.writeDouble(amount.get());
        }
        @Override
        public int compareTo(OrderBean o)
        {
            System.out.println("bean compareTo");
            int result = this.order_id.compareTo(o.getOrder_id());
            if(result==0){
                return -this.amount.compareTo(o.getAmount());
            }else{
                return result;
            }
        }
        
        @Override
        public String toString()
        {
            System.out.println("toString");
            return order_id.toString()+" "+amount.get();
        }
    }
}

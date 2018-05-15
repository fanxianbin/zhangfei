package com.thread;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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

public class XXXX
{
    
    public static class OrderMapper extends Mapper<Object,Text,Text,IntWritable>{
        
        @Override
        protected void map(Object key, Text value,Context context)
                throws IOException, InterruptedException
        {
            String line = value.toString();
            System.out.println(123);
            String[] ss = line.split(" +");
            //context.write(new OrderBean(new Text(ss[0]), Double.parseDouble(ss[2])),NullWritable.get());
            context.write(new Text("1"),new IntWritable(1));
        }
    }
    
    
    public static class OrderReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        
        @Override
        protected void reduce(Text arg0, Iterable<IntWritable> arg1, Context arg2)
                throws IOException, InterruptedException
        {
            arg2.write(arg0, new IntWritable(1));
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        Configuration conf = new Configuration();
        conf.set("hdfs.defaultFS","hdfs://localhost:9000");
        Job job = Job.getInstance(conf,"group");
        
        job.setJarByClass(XXXX.class);
        
        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(OrderReducer.class);
        
        job.setNumReduceTasks(1);
        //job.setPartitionerClass(OrderIdPartitioner.class);
        
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(NullWritable.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.setInputPaths(job,new Path("C:\\Users\\fanxianb\\Desktop\\order.txt"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Users\\fanxianb\\Desktop\\order"));
        
        boolean flag = job.waitForCompletion(true);
        System.out.println("是否成功"+flag);
    }
    
    public static class GroupComparator extends WritableComparator{
        
        public GroupComparator(){
            super(OrderBean.class,true);
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
        private double amount;
        
        public OrderBean(Text order_id, double amount)
        {
            System.out.println(12);
            set(order_id, amount);
        }
        public void set(Text order_id, double amount){
            System.out.println("11122");
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
        public double getAmount()
        {
            return amount;
        }
        public void setAmount(double amount)
        {
            this.amount = amount;
        }
        @Override
        public void readFields(DataInput in) throws IOException
        {
            this.order_id = new Text(in.readUTF());
            this.amount = in.readDouble();
        }
        @Override
        public void write(DataOutput out) throws IOException
        {
            out.writeUTF(order_id.toString());
            out.writeDouble(amount);
        }
        @Override
        public int compareTo(OrderBean o)
        {
            System.out.println("bean compareTo");
            int result = this.order_id.compareTo(o.order_id);
            if(result==0){
                return this.amount==o.amount?0:(this.amount>o.amount?1:-1);
            }else{
                return result;
            }
        }
        
        @Override
        public String toString()
        {
            System.out.println("toString");
            return order_id.toString()+" "+amount;
        }
    }
}

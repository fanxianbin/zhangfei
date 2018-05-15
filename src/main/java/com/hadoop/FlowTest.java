package com.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlowTest
{
    public static class FlowMapper extends Mapper<Object,Text,Text,FlowBean>{
        
        private final Text text = new Text();
        
        @Override
        protected void map(Object key, Text value,Context context)
                throws IOException, InterruptedException
        {
            System.out.println("xxxxxxxxxxx");
            FileSplit f = (FileSplit) context.getInputSplit();
            System.out.println(Arrays.toString(f.getLocations()));
            System.out.println(f.getPath().getName());
            String line = value.toString();
            String[] field = line.split(" +");
            System.out.println(line);
            System.out.println(Arrays.toString(field));
            text.set(field[0]);
            long dFlow = Long.parseLong(field[1]);
            long upFlow = Long.parseLong(field[2]);
            context.write(text,new FlowBean(dFlow, upFlow));
        }
    }
    
    public static class FlowReducer extends Reducer<Text,FlowBean,Text,FlowBean>{
        
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context)
                throws IOException, InterruptedException
        {
            long dFlow = 0L;
            long upFLow = 0L;
            for(FlowBean bean : values){
                dFlow  += bean.getdFlow();
                upFLow += bean.getUpFlow();
            }
            context.write(key,new FlowBean(dFlow,upFLow));
            org.apache.hadoop.mapreduce.Reducer.Context x = null;
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        Configuration conf = new Configuration();
        conf.set("hdfs.defaultFS","hdfs://localhost:9000");
        String inputPath = "C:\\Users\\fanxianb\\Desktop\\flow.txt";
        String outputPath = "C:\\Users\\fanxianb\\Desktop\\flow";
        Job job = Job.getInstance(conf, "flow");
        
        job.setJarByClass(FlowTest.class);
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);
        
        job.setNumReduceTasks(5);
        job.setPartitionerClass(ProvincePartitioner.class);
        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);
        
        FileInputFormat.setInputPaths(job,new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));
        
        boolean flag = job.waitForCompletion(true);
        System.out.println("是否成功"+flag);
    }
    
    public static class ProvincePartitioner extends Partitioner<Text,FlowBean>{

        public static Map<String,Integer> map = new HashMap<String,Integer>();
        
        static{
            map.put("137",0);
            map.put("133",1);
            map.put("138",2);
            map.put("135",3);
        }
        
        @Override
        public int getPartition(Text key, FlowBean value, int numParttitions)
        {
            System.out.println("key"+key.toString()+"numParttitions"+numParttitions);
            String numPrefix = key.toString().substring(0,3);
            Integer part = map.get(numPrefix);
            return part==null?4:part;
        }
        
    }
    
    public static class FlowBean implements Writable{
        private long dFlow;
        private long upFlow;
        //private long sumFlow;
        
        public FlowBean(){
            
        }
        
        public FlowBean(long dFlow, long upFlow)
        {
            this.dFlow = dFlow;
            this.upFlow = upFlow;
            //this.sumFlow = dFlow+upFlow;
        }
        public long getdFlow()
        {
            return dFlow;
        }
        public void setdFlow(long dFlow)
        {
            this.dFlow = dFlow;
        }
        public long getUpFlow()
        {
            return upFlow;
        }
        public void setUpFlow(long upFlow)
        {
            this.upFlow = upFlow;
        }
//        public long getSumFlow()
//        {
//            return sumFlow;
//        }
//        public void setSumFlow(long sumFlow)
//        {
//            this.sumFlow = sumFlow;
//        }
        @Override
        public void readFields(DataInput in) throws IOException
        {
            System.out.println("read");
            this.dFlow = in.readLong();
            this.upFlow = in.readLong();
        }
        @Override
        public void write(DataOutput out) throws IOException
        {
            System.out.println("write");
            out.writeLong(dFlow);
            out.writeLong(upFlow);
        }
        
        @Override
        public String toString()
        {
            //return dFlow+" "+upFlow+" "+(dFlow+upFlow);
            return NullWritable.get().toString();
        }
    }
}

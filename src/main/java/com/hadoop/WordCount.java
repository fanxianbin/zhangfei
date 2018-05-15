package com.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount
{
    
    public static class WordCountMapper extends Mapper<Object,Text,Text,IntWritable>{
        
        private IntWritable w = new IntWritable(1);
        
        private final Text t = new Text();
        
        @Override
        protected void map(Object key, Text value,Context context)
                throws IOException, InterruptedException
        {
            String line = value.toString();
            if(!line.trim().equals("")){
                String[] words = line.split(" ");
                for(String word : words){
                    t.set(word);
                    context.write(t, w);
                }
            }
        }
    }
    
    public static class WordcountReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        
        private final IntWritable w = new IntWritable();
        
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException
        {
            int count = 0;
            System.out.println(key);
            for(IntWritable w : values){
                count+=w.get();
                System.out.println("xxxxxx");
            }
            w.set(count);
            System.out.println(key);
            context.write(key,w);
        }
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException
    {
        String inputStr = null;
        if(true || args[0] == null){
            inputStr = "C:\\Users\\fanxianb\\Desktop\\a.txt";
        }else{
            inputStr = args[0];
        }
        
        String outStr = null;
        if(true || args[1] == null){
            outStr = "C:\\Users\\fanxianb\\Desktop\\zhangfei";
        }else{
            outStr = args[1];
        }
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf,"count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordcountReducer.class);
        
        job.setCombinerClass(WordcountReducer.class);
        job.setNumReduceTasks(0);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.setInputPaths(job,new Path(inputStr));
        FileOutputFormat.setOutputPath(job,new Path(outStr));
        
        boolean flag = job.waitForCompletion(true);
        System.out.println("执行完成"+flag);
    }
}

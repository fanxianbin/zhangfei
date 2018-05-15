package com.xxx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.thread.Sub;
import com.thread.Sub.XX;

public class WordCount
{
    public static void main(String[] args)
    {
        List<String> list = new ArrayList<String>();
        for(int i=0;i<1000000;i++){
            list.add("aa"+i);
        }
        long time = System.currentTimeMillis();
        String min = "aa200";
        for(String str : list){
            if(min.compareTo(str)>0){
                min=str;
            }
        }
        System.out.println(System.currentTimeMillis()-time);
        int m = 10;
        int n = 10;
        for(String str : list){
            n = Integer.parseInt(str.substring(2));
            if(m>n){
                m=n;
            }
        }
        System.out.println(System.currentTimeMillis()-time);
    }
    
    public static void test(){
        
    }
    
}

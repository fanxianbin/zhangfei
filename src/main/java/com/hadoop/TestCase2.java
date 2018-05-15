package com.hadoop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang.WordUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

public class TestCase2
{
    public static void test1() throws IOException{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://localhost:9000");
        conf.set("hadoop.job.ugi","hadoop,hadoop");
        FileSystem fs = FileSystem.get(conf);
        fs.mkdirs(new Path("/xxxx"));
        //fs.deleteOnExit(new Path("/xxxx"));
        //fs.copyFromLocalFile(new Path("C:\\Users\\fanxianb\\Desktop\\交接"), new Path("/xxxxx"));
        String input = "/user/hadoop/input";
        FileStatus f = fs.getFileStatus(new Path(input));
        System.out.println("getAccessTime:"+f.getAccessTime());
        System.out.println("getBlockSize:"+f.getBlockSize());
        System.out.println("getGroup:"+f.getGroup());
        System.out.println("getLen:"+f.getLen());
        System.out.println("getModificationTime:"+f.getModificationTime());
        System.out.println("getOwner:"+f.getOwner());
        System.out.println("getReplication:"+f.getReplication());
        System.out.println("getPath:"+f.getPath());
        System.out.println("getPermission:"+f.getPermission());
        FSDataInputStream fis = fs.open(new Path("/ceshi/a.txt"));
        String line= null;
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        FSDataOutputStream fos = fs.create(new Path("/ceshi/ax.txt"), new Progressable()
        {
            public void progress()
            {
                System.out.println(123);
            }
        });
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(fos,"utf-8"),true);
        while((line=br.readLine())!=null){
            //System.out.println(line);
            pw.println(line);
            pw.flush();
        }
        pw.close();
        br.close();
        fs.close();
    }
    
    public static void main(String[] args) throws IOException
    {
        test1();
    }
}

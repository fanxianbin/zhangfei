package com.hadoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.server.UID;
import java.util.Arrays;
import java.util.Map;

import oracle.net.aso.f;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.CreateFlag;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.util.Tool;

public class TestCase implements PathFilter
{
    
    public static void upload(String localPath) throws IOException, URISyntaxException{
        File file = new File(localPath);
        System.out.println(file.exists());
        Configuration conf = new Configuration();
        //conf.setBoolean("dfs.support.append",true);
        System.out.println(123);
        String hdfsPath = "hdfs://localhost:9000";
        String inputPath = "hdfs://localhost:9000/user/hadoop/input";
        inputPath = "/ceshi/a.txt";
        Path path = new Path(hdfsPath);
        FileSystem fs = FileSystem.get(path.toUri(), conf);
        //fs.copyFromLocalFile(false,false,new Path(localPath),new Path(inputPath));
        //FSDataOutputStream os = fs.create(new Path(inputPath));
        //fs.copyFromLocalFile(new Path(localPath),new Path(inputPath));
        //FSDataOutputStream os = fs.create(new Path(inputPath),false);
        FSDataOutputStream os = fs.create(new Path(inputPath));
        FileInputStream fis = new FileInputStream(file);
        IOUtils.copyBytes(fis,os,4096);
        DistributedFileSystem hdfs = (DistributedFileSystem) fs;
        System.out.println( hdfs.getName());
        DatanodeInfo[] dis = hdfs.getDataNodeStats();
        System.out.println("---------------------");
        for(DatanodeInfo info : dis){
            System.out.println(info.getCapacity());
        }
        fis.close();
        os.close();  
        fs.close();
        System.out.println("上传完成");
    }
    
    public static void download(String outputFile) throws IllegalArgumentException, IOException{
        Configuration conf = new Configuration();
        String hdfsPath = "hdfs://localhost:9000";
        String outPath = "hdfs://localhost:9000/user/hadoop/input";
        FileSystem fs = FileSystem.get(new Path(hdfsPath).toUri(),conf);
        fs.copyToLocalFile(new Path(outPath), new Path(outputFile));
        fs.close();
        System.out.println("下载完成");
    }
    
    public static void delete() throws IOException{
        Configuration conf = new Configuration();
        String hdfsOutput = "hdfs://localhost:9000/user/hadoop/input3";
        String hdfsPath = "hdfs://localhost:9000";
        Path path = new Path(hdfsOutput);
        FileSystem fs = FileSystem.get(URI.create(hdfsOutput), conf);
        boolean b = fs.deleteOnExit(path);
        System.out.println(b);
        fs.close();
        
        System.out.println("文件已经删除");
    }
    
    public static void getStatus() throws IllegalArgumentException, IOException{
        System.out.println(Integer.MAX_VALUE);
        String hdfsPath = "hdfs://localhost:9000";
        FileSystem fs = FileSystem.get(new Path(hdfsPath).toUri(),new Configuration());
        Map map = fs.getStatistics();
        FsStatus status = fs.getStatus();
        System.out.println(status.getCapacity());
        System.out.println(status.getRemaining());
        System.out.println(status.getUsed());
        System.out.println(map);
        FileStatus f = fs.getFileStatus(new Path("hdfs://localhost:9000/user/hadoop/input"));
        
        System.out.println("getAccessTime"+f.getAccessTime());
        System.out.println("getBlockSize"+f.getBlockSize());
        System.out.println("getGroup"+f.getGroup());
        System.out.println("getLen"+f.getLen());
        System.out.println("getModificationTime"+f.getModificationTime());
        System.out.println("getOwner"+f.getOwner());
        System.out.println("getReplication"+f.getReplication());
        System.out.println("getPath"+f.getPath());
        System.out.println("getPermission"+f.getPermission());
        //System.out.println("getSymlink"+f.getSymlink());
        FileStatus[] fss = fs.listStatus(new Path("hdfs://localhost:9000/user/hadoop/"));
        for(FileStatus xx : fss){
            System.out.println(xx.getPath());
        }
        long size = fs.getBlockSize(new Path("hdfs://localhost:9000/user/hadoop/input"));
        System.out.println(size);
        BlockLocation loc =  fs.getFileBlockLocations(f, 0,f.getLen())[0];
        System.out.println(loc.getLength());
        System.out.println(loc.getHosts()[0]);
        FSDataInputStream is = fs.open(null);
    }
    
    public static void testSequence() throws IOException, URISyntaxException{
        String filePath = "hdfs://localhost:9000/user/hadoop/testSeq3";
        IntWritable k = new IntWritable();
        Text v = new Text();
        Configuration conf = new Configuration();
        
        FSDataOutputStream os = FileSystem.get(new URI(filePath), conf).create(new Path(filePath));
        Writer w = SequenceFile.createWriter(conf,
                Writer.file(new Path(filePath)),
                Writer.keyClass(k.getClass()),
                Writer.valueClass(v.getClass()),
                Writer.bufferSize(4096),
                Writer.compression(CompressionType.BLOCK));
        for(int i=0;i<100;i++){
            k.set(i);
            v.set(" bbb ");
            w.append(k, v);
        }
        w.close();
        Reader read = new SequenceFile.Reader(conf,Reader.file(new Path(filePath)));
        IntWritable kk = new IntWritable();
        Text vv = new Text();
        while(read.next(kk,vv)){
            System.out.println(kk.get()+"=="+new String(vv.getBytes()));
        }
        FileSystem fs = null;
        DistributedFileSystem dfs = null;
        read.close();
    }
    
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        //getStatus();
        upload("C:\\Users\\fanxianb\\Desktop\\a.txt");
        //String path = "";
        //testSequence();
        //upload("C:\\Users\\fanxianb\\Desktop\\a.txt");
        //upload("‪C:\\Users\\fanxianb\\Desktop\\a.txt");
        //download("C:\\Users\\fanxianb\\Desktop\\aa.txt");
        //delete();
//        File file = new File("C:\\Users\\fanxianb\\Desktop\\a.txt");
//        System.out.println(file.toURI());
        //new URI("file:C:\\Users\\fanxianb\\Desktop\\a.txt");
//        System.out.println(file.exists());
    }

    public boolean accept(Path arg0)
    {
        return false;
    }

}

package com.thread;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.server.UID;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCase
{
    
    public static void main(String[] args) throws InterruptedException, IOException
    {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\fanxianb\\Desktop\\test.txt");
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF("卧槽");
        dos.writeUTF("你妹的");
        dos.writeUTF("你妹");
        dos.writeUTF("你妹 的2");
        dos.writeUTF("你妹的3");
        dos.close();
    }
}

package com.thread;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.xxx.P;

public class Sub extends P
{
    @Override
    protected void test(com.xxx.P.TT list)
    {
        System.out.println("sub");
    }
    
    public static class XX extends TT{
        
    }
    public static void main(String[] args) throws IOException
    {
        DataInputStream dis = new DataInputStream(new FileInputStream("C:\\Users\\fanxianb\\Desktop\\test.txt"));
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
    }
}

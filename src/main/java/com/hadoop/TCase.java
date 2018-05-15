package com.hadoop;

import org.apache.hadoop.io.Text;

public class TCase
{
    public static void main(String[] args)
    {
        Text text = new Text();
        text.set("xasx");
        text.set("xxx");
        text.append("xasx".getBytes(),0, 2);
        System.out.println(new String(text.getBytes()));
    }
}

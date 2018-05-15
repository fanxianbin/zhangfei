package com.huahudie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;

import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;

import oracle.jdbc.rowset.OracleCachedRowSet;

public class TestCase implements Tool
{
    public static void main(String[] args) throws SQLException, IOException
    {
        Date d = new Date();
        OracleCachedRowSet s = new OracleCachedRowSet();
        s.toString();
        MysqlClearPasswordPlugin p = new MysqlClearPasswordPlugin();
        p.toString();
        System.out.println(System.getProperty("java.class.path"));
        //System.out.println(path.getPath());
        URL u = TestCase.class.getClassLoader().getResource("a.txt");
        System.out.println(u.getPath());
        File directory = new File(u.getPath());// 参数为空
        //System.out.println(directory.exists());
        System.out.println("文件是否存在"+directory.exists());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("测试成功,今天是"+sdf.format(d));
        ClassLoader c = null;
    }

    public void setConf(Configuration configuration)
    {
        
    }

    public Configuration getConf()
    {
        return null;
    }

    public int run(String[] as) throws Exception
    {
        return 0;
    }
}

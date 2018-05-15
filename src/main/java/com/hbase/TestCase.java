package com.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.tools.classfile.StackMapTable_attribute.verification_type_info;

public class TestCase
{
    private static Configuration conf = HBaseConfiguration.create();
    
    private static Connection conn = null;
    
    static{
        try
        {
            conf.set("hbase.zookeeper.property.clientPort","2181");
            conn = ConnectionFactory.createConnection(conf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void test() throws IOException{
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        Admin ad = conn.getAdmin();
        System.out.println(admin.getMasterInfoPort());
        TableName table = TableName.valueOf("xxxx");
        System.out.println(table.getQualifierAsString());
        System.out.println(admin.tableExists(table));
        
        HTableDescriptor des = admin.getTableDescriptor(table);
        System.out.println(des.getFamilies());
        Collection<HColumnDescriptor> c = des.getFamilies();
        System.out.println("=======start========");
        for(HColumnDescriptor desc : c){
            System.out.println(desc.getMaxVersions());
            System.out.println(desc.getMinVersions());
        }
        System.out.println("========end=========");
        HColumnDescriptor col = new HColumnDescriptor("test");
        col.setMaxVersions(5);
        col.setMinVersions(2);
        col.setTimeToLive(10);
        admin.addColumn("xxxx",col);
        admin.close();
    }
    
    public static void test2() throws IOException{
        HTable table = new HTable(conf,"user");
        Get get = new Get("row1".getBytes());
        Result result = table.get(get);
        List<Cell> cells = result.listCells();
        for(Cell c : cells){
            System.out.println(c);
        }
    }
    
    public static void test3() throws IOException{
        long time=  System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            new HTable(conf,"user");
        }
        System.out.println(System.currentTimeMillis()-time);
//        HTablePool pool = new HTablePool(conf,100);
//        HTableInterface table = pool.getTable("user");
//        Table t = conn.getTable(null);
        Table table = conn.getTable(TableName.valueOf("user"));
        Get get = new Get("row2".getBytes());
        get.addColumn("info".getBytes(),"name".getBytes());
        Result result = table.get(get);
        print(result);
        //List<Cell> list = result.listCells();
        conn.close();
    }
    
    public static void print(Result result){
        KeyValue[] list = result.raw();
        for(KeyValue cell : list){
//            System.out.println(cell.getClass());
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>");
//            System.out.println(cell.toString());
//            System.out.println(new String(cell.getFamilyArray()));
//            System.out.println(new String(cell.getQualifierArray()));
//            System.out.println(new String(cell.getRowArray()));
//            System.out.println(new String(cell.getTagsArray()));
//            System.out.println(new String(cell.getValueArray()));
//            System.out.println(".....................");
            System.out.println(new String(cell.getFamily())+":"+new String(cell.getQualifier())+"="+new String(cell.getValue()));
//            System.out.println(new String(cell.getRow()));
//            System.out.println(new String(cell.getTagsArray()));
        }
    }
    
    public static void test4() throws IOException{
        Table table = conn.getTable(TableName.valueOf("user"));
        Put put = new Put("row2".getBytes());
        put.addColumn("info".getBytes(),"name".getBytes(),"张菲".getBytes());
        System.out.println(table.getClass());
        table.put(put);
        table.close();
        conn.close();
    }
    
    public static void test5() throws IOException{
        HTablePool pool = new HTablePool(conf,10);
        Table table = pool.getTable("user");
        Delete delete = new Delete("row2".getBytes());
        delete.addColumn("info".getBytes(),"name".getBytes());
        table.delete(delete);
        pool.close();
        
    }
    
    public static void test6() throws IOException{
        HTablePool pool = new HTablePool(conf,10);
        Table table = pool.getTable("user");
        Scan scan = new Scan();
        String filterStr = "neijiang";
        FilterList filter = new FilterList(Operator.MUST_PASS_ALL);
        SingleColumnValueFilter f1 = new SingleColumnValueFilter("address".getBytes(),"city".getBytes(),CompareOp.EQUAL,filterStr.getBytes());
        SingleColumnValueFilter f2 = new SingleColumnValueFilter("info".getBytes(),"name".getBytes(),CompareOp.EQUAL,"fan".getBytes());
        KeyOnlyFilter f3 = new KeyOnlyFilter();
        filter.addFilter(f2);
        filter.addFilter(f1);
        //filter.addFilter(f3);
        scan.setFilter(filter);
        
        scan.addColumn("address".getBytes(),"city".getBytes());
        scan.addColumn("info".getBytes(),"name".getBytes());
        ResultScanner results = table.getScanner(scan);
        Iterator<Result> it = results.iterator();
        while(it.hasNext()){
            print(it.next());
        }
        pool.close();
    }
    
    public static void test7() throws IOException{
        Admin admin = conn.getAdmin();
    }
    
    public static void main(String[] args) throws IOException
    {
        test6();
    }
    
    
}

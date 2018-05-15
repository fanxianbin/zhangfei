package com.zk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.Transaction;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;

public class TestCase
{
    private static Watcher watcher = new TestWatcher();
    
    
    static{
        try
        {
            long time = System.currentTimeMillis();
            ZooKeeper zk = null;
            for(int i=0;i<0;i++){
                zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:3181,127.0.0.1:4181",3000, watcher);
            }
            System.out.println(System.currentTimeMillis()-time);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException
    {
        final String node = "/node2";
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181",8000, watcher);
        String path = node+"/xxx";
        zk.getData(path, new TestWatcher(), null);
        Thread th= new Thread(new Runnable(){

            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }
    
    public static class TestWatcher implements Watcher{

        @Override
        public void process(WatchedEvent e)
        {
            try{
                if(KeeperState.SyncConnected==e.getState()){
                    if(e.getType()==EventType.None && e.getPath()==null){
                        //连接建立
                        System.out.println("---------connect build----------");
                    }else if(EventType.NodeCreated==e.getType()){
                        System.out.println("--------------阶段创建------------------");
                    }else if(EventType.NodeDeleted==e.getType()){
                        System.out.println("--------------节点删除-----------------");
                    }else if(EventType.NodeDataChanged==e.getType()){
                        System.out.println("--------------节点值变动-------------------");
                        //System.out.println(new String(zk.getData(e.getPath(),true, null)));
                    }else if(EventType.NodeChildrenChanged == e.getType()){
                        System.out.println("--------------子节点增删---------------------");
                    }else{
                        System.out.println("--------------未知事件-----------------------");
                    }
                }else{
                    System.out.println("-----------------------状态不对---------------");
                }
                //Thread.sleep(1900);
            }catch(Exception ee){
                ee.printStackTrace();
            }
//            System.out.println(e);
//            System.out.println(e.getPath());
//            System.out.println(e.getState()==KeeperState.SyncConnected);
//            System.out.println(e.getType());
//            System.out.println(e.getWrapper());
        }
        
    }
}

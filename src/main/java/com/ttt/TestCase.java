package com.ttt;

import java.util.concurrent.CountDownLatch;

import oracle.net.aso.e;

public class TestCase
{
    public static void main(String[] args) throws InterruptedException
    {
        final CountDownLatch latch = new CountDownLatch(1);
        Thread t = new Thread(new Runnable(){

            @Override
            public void run()
            {
                try
                {
                    Thread.currentThread().interrupt();
//                    for(int i=0;i<1000000;i++){
//                        System.out.println(11111);
//                    }
                    //Thread.sleep(1000);
                    latch.await();
                    System.out.println(123);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
//        System.out.println(123233);
        //Thread.sleep(1234);
        //latch.countDown();
//        t.interrupted();
    }
}

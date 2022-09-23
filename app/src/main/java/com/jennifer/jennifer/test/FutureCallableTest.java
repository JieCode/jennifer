package com.jennifer.jennifer.test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author jingjie
 * @date :2021/11/19 15:21
 * TODO:
 */
public class FutureCallableTest {
    public static void main(String[] args) {
        //创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        Future<Integer> future1 = threadPool.submit(new HandleCallable("线程1 "));
        Future<Integer> future2 = threadPool.submit(new HandleCallable("线程2 "));

        //把线程池关闭，然后等待线程池里面的线程执行完毕
        threadPool.shutdown();
        while (!threadPool.isTerminated()){

        }
        //获取执行的结果
        try {
            System.out.println("线程1 的执行结果 sum = "+future1.get());
            System.out.println("线程2 的执行结果 sum = "+future2.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("不阻塞");
    }
}

class HandleCallable implements Callable<Integer>{
    String callableName;

    public HandleCallable(String callableName) {
        this.callableName = callableName;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println(callableName+System.currentTimeMillis()+"开始");
        Thread.sleep(1000);
        int random = new Random().nextInt(30);
        System.out.println(callableName+System.currentTimeMillis()+" 随机数>>>"+random);
        int sum = 0;
        for (int i = 1; i <= random; i++) {
            Thread.sleep(100);
            sum +=i;
        }
        System.out.println(callableName+System.currentTimeMillis()+"结束");
        return sum;
    }
}

package com.jennifer.jennifer.ui.palette.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wutongtech_shengmao on 2017/6/24 15:54.
 * 作用：线程池工具类 --- 主要用于画板中的绘制操作
 */

public class ThreadPoolUtils {

    private ExecutorService executor;

    private static ThreadPoolUtils utils = new ThreadPoolUtils();

    private ThreadPoolUtils(){
        executor = Executors.newFixedThreadPool(3);
    }

    public static ThreadPoolUtils getInstance(){
        return utils;
    }

    public void execute(Runnable runnable){
        executor.execute(runnable);
    }


}

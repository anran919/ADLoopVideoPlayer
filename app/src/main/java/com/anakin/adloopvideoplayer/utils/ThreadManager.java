package com.anakin.adloopvideoplayer.utils;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by penghuilou on 2016/1/25.
 * 线程管理类，因为android对于线程的支持不是好，所以尽量少开线程，所以创建了一个线程管理类，用来维护线程，把程序中使用的线程放到一个线程池中
 * ThreadManager 应该设置为单例模式
 */
public class ThreadManager {
    private ThreadManager(){}
    private static ThreadManager instance = new ThreadManager();

    private ThreadPoolProxy mShortThreadPool;
    private ThreadPoolProxy mLongThreadPool;

    /**
     * 获取短线程池，多用于操作本地文件
     * @return 线程数较小线程池
     */
    public synchronized ThreadPoolProxy getmShortThreadPool() {
        if(mShortThreadPool == null){
            mShortThreadPool = new ThreadPoolProxy(3,3,5000l);
        }
        return mShortThreadPool;
    }

    /**
     * 获取长线程池，多用于请求网络资源
     * @return 线程数较多的线程池
     */
    public synchronized ThreadPoolProxy getmLongThreadPool() {
        if(mLongThreadPool == null){
            mLongThreadPool = new ThreadPoolProxy(10,10,5000l);
        }
        return mLongThreadPool;
    }

    /**
     * 获取线程管理对象
     * @return 线程管理者
     */
    public static ThreadManager getInstance(){
        return instance;
    }

    /**
     * 线程池代理类
     */
    public class ThreadPoolProxy{


        private ThreadPoolExecutor mPool;
        private int mCorePoolSize;
        private int mMaximumPoolSize;
        private long mKeepAliveTime;

        /**
         * 构造函数
         * @param mCorePoolSize 线程池维护的线程数
         * @param mMaximumPoolSize 额外开启线程数
         * @param mKeepAliveTime 当线程池中的线程为空时，线程池的存活时间
         */
        public ThreadPoolProxy(int mCorePoolSize, int mMaximumPoolSize, long mKeepAliveTime) {
            this.mCorePoolSize = mCorePoolSize;
            this.mMaximumPoolSize = Integer.MAX_VALUE;
            this.mKeepAliveTime = mKeepAliveTime;
        }

        /***
         * 执行一个异步线程
         * @param runnable 要执行的异步任务
         */
        public void excute(Runnable runnable){
            if(mPool==null){
                mPool = new ThreadPoolExecutor(mCorePoolSize,mMaximumPoolSize,mKeepAliveTime, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(10));
            }
            if(!mPool.isShutdown() && !mPool.isTerminated()){
                mPool.execute(runnable);
            }

        }

        /**
         * 取消一个线程
         * @param runnabel 要取消的异步任务
         */
        public void cancle(Runnable runnabel){
            if(mPool!=null&&!mPool.isShutdown()&&!mPool.isTerminated()){
                mPool.remove(runnabel);
            }
        }
    }
}

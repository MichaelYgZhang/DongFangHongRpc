package com.home.DongFangHongRpc.common;

import java.util.concurrent.*;

import org.apache.log4j.Logger;

public final class ThreadPoolHelper {
	private static final Logger LOG = Logger.getLogger(ThreadPoolHelper.class);
	/**
	 * 最小线程数
	 */
	private final static int DEFAULT_COREPOOLSIZE = 5;
	/**
	 * 最大线程数
	 */
	private final static int DEFAULT_MAXIMUMPOOLSIZE = 30;
	/**
	 * 线程存活时长
	 */
	private final static int DEFAULT_KEEPALIVETIME = 2;
	/**
	 * 线程存活时长的时间单位
	 */
	private final static TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;
	/**
	 * 任务队列长度
	 */
	private final static int DEFAULT_WORKQUEUESIZE = 10000;
	/**
	 * 扫描任务队列是否满了的时间间隔
	 */
	private final static int WAIT_FOR_WORK_QUEUE_MILLISECONDS = 1000;

	private BlockingDeque<Runnable> workQueue;
	private ThreadPoolExecutor executor;
	private String poolName;

	public ThreadPoolHelper(String poolName) {
		this(poolName, DEFAULT_COREPOOLSIZE, DEFAULT_WORKQUEUESIZE);
	}

	public ThreadPoolHelper(String poolName, int poolSize) {
		this(poolName, poolSize, DEFAULT_WORKQUEUESIZE);
	}

	public ThreadPoolHelper(String poolName, int poolSize, int queueSize) {
		this.poolName = poolName;
		this.workQueue = new LinkedBlockingDeque<Runnable>(DEFAULT_WORKQUEUESIZE);
		this.executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE,
				DEFAULT_MAXIMUMPOOLSIZE, DEFAULT_KEEPALIVETIME, DEFAULT_UNIT,
				workQueue,
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void submit(Runnable job) {
		if (workQueue.size() >= DEFAULT_WORKQUEUESIZE) {
			while (workQueue.size() >= DEFAULT_WORKQUEUESIZE) {
				try {
					Thread.sleep(WAIT_FOR_WORK_QUEUE_MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		executor.submit(job);
	}

	public void shutdown() {
		workQueue.clear();
		executor.shutdown();
	}
}

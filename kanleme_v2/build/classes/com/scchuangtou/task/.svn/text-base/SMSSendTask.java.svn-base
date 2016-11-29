package com.scchuangtou.task;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.scchuangtou.helper.SMSVerifyCodeHelper;
import com.scchuangtou.utils.LogUtils;

public final class SMSSendTask {
	private static final int TASK_COUNT = 5;
	private static final int[] RETRY_TIMES = new int[] { 1000, 5 * 1000, 10 * 1000, 30 * 1000, 60 * 1000 };
	private static final PriorityBlockingQueue<SMSParam> messageQueue = new PriorityBlockingQueue<>();
	private static final ArrayList<Task> tasks = new ArrayList<>();
	private static boolean stoped = true;

	public static synchronized void start() {
		if (!stoped) {
			return;
		}
		stoped = false;
		for (int i = 0; i < TASK_COUNT; i++) {
			Task task = new Task(messageQueue);
			tasks.add(task);
			task.start();
		}
		LogUtils.log("SMS task is run");
	}

	public static synchronized void stop() {
		if (stoped) {
			return;
		}
		stoped = true;
		for (Task task : tasks) {
			task.quit();
		}
		tasks.clear();
		LogUtils.log("SMS task is stoped");
	}

	public static synchronized void addMessage(SMSParam msg) {
		if (stoped) {
			return;
		}
		messageQueue.add(msg);
	}

	private static class Task extends Thread {
		private volatile boolean mQuit = false;
		private final BlockingQueue<SMSParam> mQueue;

		public Task(BlockingQueue<SMSParam> mQueue) {
			this.mQueue = mQueue;
		}

		public void quit() {
			mQuit = true;
			try {
				interrupt();
				join();
			} catch (Exception e) {
			}
		}

		@Override
		public void run() {
			SMSParam msg = null;
			while (!mQuit) {
				try {
					msg = mQueue.take();
				} catch (InterruptedException e) {
				}
				if (msg != null) {
					for (int i = 0; i < RETRY_TIMES.length; i++) {
						boolean isok = pushMessage(msg);
						if (!mQuit && !isok) {
							try {
								sleep(RETRY_TIMES[i]);
							} catch (Exception e) {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
		}

		private boolean pushMessage(SMSParam param) {
			return SMSVerifyCodeHelper.sendSMS(param.phone, param.msg);
		}
	}

	public static class SMSParam {
		private String phone;
		private String msg;

		public SMSParam(String phone, String msg) {
			this.phone = phone;
			this.msg = msg;
		}
	}
}

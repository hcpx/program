package com.scchuangtou.task;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import com.scchuangtou.config.Config;
import com.scchuangtou.helper.MipushMessageHelper;
import com.scchuangtou.utils.LogUtils;

public final class MessageTask {
	private static final int TASK_COUNT = 5;
	private static final int[] RETRY_TIMES = new int[] { 1000, 5 * 1000, 10 * 1000, 30 * 1000, 60 * 1000 };
	private static final PriorityBlockingQueue<MessageParam> messageQueue = new PriorityBlockingQueue<MessageParam>();
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
		LogUtils.log("message task is run");
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
		LogUtils.log("message task is stoped");
	}

	public static synchronized void addMessage(MessageParam param) {
		if (stoped) {
			return;
		}
		messageQueue.add(param);
	}

	private static class Task extends Thread {
		private volatile boolean mQuit = false;
		private final BlockingQueue<MessageParam> mQueue;

		public Task(BlockingQueue<MessageParam> mQueue) {
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
			MessageParam param = null;
			while (!mQuit) {
				try {
					param = mQueue.take();
				} catch (InterruptedException e) {
				}
				if (param != null) {
					for (int i = 0; i < RETRY_TIMES.length; i++) {
						boolean isok = pushMessage(param);
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

		private boolean pushMessage(MessageParam param) {
			switch (param.os) {
			case ANDROID:
				if (param.getAliasSize() > 0) {
					return MipushMessageHelper.sendAndroidMessage(param);
				} else {
					System.out.println("message:" + param.description + ",no alias");
				}
			case IOS:
				if (param.getAliasSize() > 0) {
					return MipushMessageHelper.sendIosMessage(param);
				} else {
					System.out.println("message:" + param.description + ",no alias");
				}
			default:
				return MipushMessageHelper.sendAllMessage(param);
			}
		}
	}

	public enum MessageOs {
		ANDROID, IOS, ALL
	}

	public static MessageOs getOs(int os) {
		if (os == Config.Os.IOS) {
			return MessageOs.IOS;
		} else {
			return MessageOs.ANDROID;
		}
	}

	public static class MessageParam extends MipushMessageHelper.MessageParam implements Comparable<MessageParam> {
		private MessageOs os;

		public MessageParam(MessageOs os, int message_type, String messageid) {
			this.os = os;
			extras.put("msg_id", messageid);
			extras.put("msg_type", String.valueOf(message_type));
		}

		public void addExtraId(String extraId) {
			extras.put("obj_id", extraId);
		}

		@Override
		public int compareTo(MessageParam arg0) {
			return 0;
		}
	}
}

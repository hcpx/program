package com.scchuangtou.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogUtils {
	private static final long FILE_MAX_SIZE = 5*1024*1024l;
	private static File logFile;

	public static synchronized void init(String dir) {
		if (logFile != null) {
			return;
		}
		logFile = new File(dir, "log.txt");
	}

	public synchronized static void log(Throwable t) {
		log(null,t);
	}
	
	public synchronized static void log(String msg,Throwable t) {
		if (t == null) {
			return;
		}
		if (logFile != null) {
			OutputStream os = null;
			try {
				if(logFile.length() > FILE_MAX_SIZE){
					os = new FileOutputStream(logFile);
				}else{
					os = new FileOutputStream(logFile,true);
				}
				os.write(DateUtil.formatDate(System.currentTimeMillis(), DateUtil.FORMAT_YYYYMMDDH24MM).getBytes());
				os.write("\r\n".getBytes());
				if(msg != null && msg.length() > 0){
					os.write(msg.getBytes());
					os.write("\r\n".getBytes());
				}
				t.printStackTrace(new PrintStream(os));
				os.write("\r\n\r\n".getBytes());
				os.flush();
			} catch (Exception e) {
			}finally {
				StreamUtils.closeIO(os);
			}
		}
		t.printStackTrace();
	}

	public synchronized static void log(String log) {
		if (log == null || log.length() == 0) {
			return;
		}
		if (logFile != null) {
			OutputStream os = null;
			try {
				if(logFile.length() > FILE_MAX_SIZE){
					os = new FileOutputStream(logFile);
				}else{
					os = new FileOutputStream(logFile,true);
				}
				os.write(DateUtil.formatDate(System.currentTimeMillis(), DateUtil.FORMAT_YYYYMMDDH24MM).getBytes());
				os.write("\t".getBytes());
				os.write(log.getBytes());
				os.write("\r\n\r\n".getBytes());
				os.flush();
			} catch (Exception e) {
			}finally {
				StreamUtils.closeIO(os);
			}
		}
		System.err.println(log);
	}
}

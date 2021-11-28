package week4.way13;

import java.util.concurrent.locks.LockSupport;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 利用Semaphore类来保证buffer类中size的可见性，但是Semaphore被创建为独占锁，效果与synchronized类似
 */
public class MainThread13 {
	public static Buffer13 buffer = new Buffer13();
	
	public static void main(String[] args) {
		MakeSomethingThread13 subThread = new MakeSomethingThread13();
		subThread.start();
		while(true) {
			try {
				if (buffer.size() != 0) {
					String product = buffer.get(0);
					System.out.println("mainThread GET something -> " + product);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("mainThread says byebye");
	}
}

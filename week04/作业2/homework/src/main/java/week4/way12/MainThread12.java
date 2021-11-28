package week4.way12;

import java.util.concurrent.locks.LockSupport;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 通过LockSupport类阻塞主线程，当子线程生产完数据后，再唤醒主线程读取数据。
 */
public class MainThread12 {
	public static Buffer12 buffer = new Buffer12();
	
	public static void main(String[] args) {
		MakeSomethingThread12 subThread = new MakeSomethingThread12(Thread.currentThread());
		subThread.start();
		LockSupport.park();
		while(true) {
			if (buffer.size() != 0) {
				String product = buffer.get(0);
				System.out.println("mainThread GET something -> " + product);
				break;
			}
		}
		System.out.println("mainThread says byebye");
	}
}

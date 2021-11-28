package week4.way14;

import java.util.concurrent.CountDownLatch;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 利用CountDownLatch类来保证“主线程在子线程写入数据之后，再访问缓存区”
 */
public class MainThread14 {
	public static Buffer14 buffer = new Buffer14();
	
	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		MakeSomethingThread14 subThread = new MakeSomethingThread14(countDownLatch);
		subThread.start();
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (buffer.size() != 0) {
			String product = buffer.get(0);
			System.out.println("mainThread GET something -> " + product);
		}
		System.out.println("mainThread says byebye");
	}
}

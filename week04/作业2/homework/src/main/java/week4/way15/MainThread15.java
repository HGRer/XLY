package week4.way15;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 利用CyclicBarrier保证写线程完成之后才访问缓存区，缓存中利用了AtomicInteger保证多线程的写和读安全。
 */
public class MainThread15 {
	public static Buffer15 buffer = new Buffer15();
	
	public static void main(String[] args) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> MainThread15.showBuffer());
		
		MakeSomethingThread t1 = new MakeSomethingThread(cyclicBarrier);
		MakeSomethingThread t2 = new MakeSomethingThread(cyclicBarrier);
		t1.start();
		t2.start();

		System.out.println("mainThread says byebye");
	}
	
	public static void showBuffer() {
		if (buffer.size() != 0) {
			System.out.println("showBuffer [");
			for (int i = 0; i < buffer.size(); i++) {
				System.out.println(buffer.get(i));
			}
			System.out.println("]");
		}
	}
	
	public static class MakeSomethingThread extends Thread {
		private CyclicBarrier cyclicBarrier;
		
		public MakeSomethingThread(CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
		}
		
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MainThread15.buffer.add("made in " + Thread.currentThread().getName());
			try {
				cyclicBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " finished");
		}
	}
}

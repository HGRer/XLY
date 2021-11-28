package week4.way14;

import java.util.concurrent.CountDownLatch;

public class MakeSomethingThread14 extends Thread {
	private CountDownLatch countDownLatch;
	
	public MakeSomethingThread14(CountDownLatch countDownLatch) {
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread14.buffer.add(makeSomething());
		countDownLatch.countDown();
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

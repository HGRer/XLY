package week4.way13;

import java.util.concurrent.locks.LockSupport;

public class MakeSomethingThread13 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread13.buffer.add(makeSomething());
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

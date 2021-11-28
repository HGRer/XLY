package week4.way12;

import java.util.concurrent.locks.LockSupport;

public class MakeSomethingThread12 extends Thread {
	private Thread threadToUnPark;
	
	public MakeSomethingThread12(Thread threadToUnPark) {
		this.threadToUnPark = threadToUnPark;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread12.buffer.add(makeSomething());
		LockSupport.unpark(threadToUnPark);
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

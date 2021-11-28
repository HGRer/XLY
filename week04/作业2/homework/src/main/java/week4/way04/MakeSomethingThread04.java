package week4.way04;

public class MakeSomethingThread04 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread04.buffer.add(makeSomething());
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

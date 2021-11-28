package week4.way02;

public class MakeSomethingThread02 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread02.buffer.add(makeSomething());
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

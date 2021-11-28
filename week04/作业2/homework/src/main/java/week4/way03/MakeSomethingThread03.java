package week4.way03;

public class MakeSomethingThread03 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread03.buffer.add(makeSomething());
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

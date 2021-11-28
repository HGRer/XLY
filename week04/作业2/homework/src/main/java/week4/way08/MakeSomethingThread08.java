package week4.way08;

public class MakeSomethingThread08 extends Thread {
	@Override
	public void run() {
		MainThread08.buffer.add(makeSomething());
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

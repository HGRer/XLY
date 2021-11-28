package week4.way07;

public class MakeSomethingThread07 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainThread07.buffer.add(makeSomething());
		
		synchronized (MainThread07.obj) {
			MainThread07.obj.notifyAll();
		}
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

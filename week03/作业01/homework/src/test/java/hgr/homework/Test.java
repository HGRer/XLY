package hgr.homework;

public class Test {
	public volatile static int x = 1;
	
	public static void main(String[] args) throws Exception {
		Thread t1 = new Thread(() -> {
			int a = x;
			a++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			x = a;
			System.out.println(Thread.currentThread().getName() + " x -> " + x);
		});
		
		Thread t2 = new Thread(() -> {
			int a = x;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			a++;
			x = a;
			System.out.println(Thread.currentThread().getName() + " x -> " + x);
		});
		t1.start();
		Thread.sleep(100);
		t2.start();
		Thread.sleep(100);
	}
	
}

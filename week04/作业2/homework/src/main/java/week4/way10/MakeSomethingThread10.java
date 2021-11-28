package week4.way10;

import java.util.concurrent.Callable;

public class MakeSomethingThread10 implements Callable<String> {
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}



	@Override
	public String call() throws Exception {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return makeSomething();
	}
}

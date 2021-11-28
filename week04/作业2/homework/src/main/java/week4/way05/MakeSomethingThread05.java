package week4.way05;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class MakeSomethingThread05 extends Thread {
	@Override
	public void run() {
		try {
			Thread.sleep(1000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		URL url = MakeSomethingThread05.class.getClassLoader().getResource("bufferfile.txt");
		try {
			File file = new File(url.toURI());
			FileOutputStream out = new FileOutputStream(file);
			out.write(makeSomething().getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String makeSomething() {
		String product = "Hello World! Made by makeSomethingThread";
		return product;
	}
}

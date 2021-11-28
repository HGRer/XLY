package week4.way05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * 设计思路：
 * 通过txt文件来模拟中间件来让线程间通信，通过这个txt文件的属性来实现“同步”，让读线程看到写线程的结果
 */
public class MainThread05 {
	
	public static void main(String[] args) {
		cleanFile();
		
		MakeSomethingThread05 makeSomethingThread = new MakeSomethingThread05();
		makeSomethingThread.start();
		
		while (true) {
			try (InputStream input = MainThread05.class.getClassLoader().getResourceAsStream("bufferfile.txt")) {
				if (input != null && input.available() > 0) {
					byte[] byteArray = new byte[16];
					input.read(byteArray);
					System.out.println(new String(byteArray));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("mainThread5 says byebye");
	}
	
	public static void cleanFile() {
		URL url = MainThread05.class.getClassLoader().getResource("bufferfile.txt");
		try {
			File file = new File(url.toURI());
			FileOutputStream out = new FileOutputStream(file);
			out.write("".getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

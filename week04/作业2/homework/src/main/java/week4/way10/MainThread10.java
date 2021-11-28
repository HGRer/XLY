package week4.way10;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询future直到读取到数据为止。
 * 
 */
public class MainThread10 {
	
	public static void main(String[] args) {
		MakeSomethingThread10 subThread = new MakeSomethingThread10();
		ExecutorService es = Executors.newSingleThreadExecutor();
		Future<String> future = es.submit(subThread);
		es.shutdown();
		
		while (true) {
			String str;
			try {
				str = future.get();
				if (str != null) {
					System.out.println(str);
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("mainThread says byebye");
	}
}

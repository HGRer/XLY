package week4.way04;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 设计思路：
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * 
 * 主线程无法看见写线程对共享变量的改变
 * 使用CopyOnWriteArrayList的CAS机制来获取
 */
public class MainThread04 {
	public static CopyOnWriteArrayList<String> buffer = new CopyOnWriteArrayList<>();
	
	public static void main(String[] args) {
		MakeSomethingThread04 subThread = new MakeSomethingThread04();
		subThread.start();
		
		while(true) {
			if (buffer.size() != 0) {
				String product = buffer.get(0);
				System.out.println("mainThread GET something -> " + product);
				break;
			}
		}
		System.out.println("mainThread says byebye");
	}
}

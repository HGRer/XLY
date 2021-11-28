package week4.way01;

import java.util.ArrayList;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * -情况1：当子线程无添加Thread.sleep，主线程在缓存区取到数据
 * -情况2：当子线程添加了Thread.sleep，主线程一直在轮询
 * -情况3：在情况2的基础上，对缓存区buffer变量增了volatile后，主线程从缓冲区读取到数据
 * 
 * 主线程无法看见写线程对共享变量的改变
 * 解决：使用volatile来修饰变量buffer，保证变量buffer的内存可见性
 */
public class MainThread01 {
	public static volatile Buffer01 buffer = new Buffer01();
	
	public static void main(String[] args) {
		MakeSomethingThread01 subThread = new MakeSomethingThread01();
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

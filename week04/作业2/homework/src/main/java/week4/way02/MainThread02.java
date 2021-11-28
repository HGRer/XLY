package week4.way02;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * 使用volatile来修饰Buffer02类中的成员变量size，保证变量size的内存可见性
 */
public class MainThread02 {
	public static Buffer02 buffer = new Buffer02();
	
	public static void main(String[] args) {
		MakeSomethingThread02 subThread = new MakeSomethingThread02();
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

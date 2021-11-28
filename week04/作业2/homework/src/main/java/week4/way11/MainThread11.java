package week4.way11;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 在buffer11中使用可重入的读写锁，仅对size变量使用了读锁。
 */
public class MainThread11 {
	public static Buffer11 buffer = new Buffer11();
	
	public static void main(String[] args) {
		MakeSomethingThread11 subThread = new MakeSomethingThread11();
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

package week4.way06;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * 在Buffer03中，利用synchronized关键字来保证原子性和可见性
 */
public class MainThread06 {
	public static Buffer06 buffer = new Buffer06();
	
	public static void main(String[] args) {
		MakeSomethingThread06 subThread = new MakeSomethingThread06();
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

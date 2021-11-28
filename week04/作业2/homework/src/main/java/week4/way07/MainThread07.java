package week4.way07;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * 主线程启动一个子线程，然后进入等待，直到被子线程唤醒去读取数据。
 */
public class MainThread07 {
	public static Buffer07 buffer = new Buffer07();
	public static Object obj = new Object();
	
	public static void main(String[] args) {
		MakeSomethingThread07 subThread = new MakeSomethingThread07();
		subThread.start();
		
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
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

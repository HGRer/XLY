package week4.way08;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 通过sleep方法阻塞主线程，只有当子线程运行完之后，主线程才去访问缓冲区
 */
public class MainThread08 {
	public static Buffer08 buffer = new Buffer08();
	
	public static void main(String[] args) {
		MakeSomethingThread08 subThread = new MakeSomethingThread08();
		subThread.start(); // 子线程不再sleep等待
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
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

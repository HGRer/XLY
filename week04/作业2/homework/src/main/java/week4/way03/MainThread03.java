package week4.way03;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 线程会不断轮询该缓冲区直到读取到数据为止。
 * 在Buffer03中，使用了原子类来确保变量size的可见性
 */
public class MainThread03 {
	public static Buffer03 buffer = new Buffer03();
	
	public static void main(String[] args) {
		MakeSomethingThread03 subThread = new MakeSomethingThread03();
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

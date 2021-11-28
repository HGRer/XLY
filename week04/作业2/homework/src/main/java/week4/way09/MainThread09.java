package week4.way09;

/**
 * 设计思路：
 * 缓存区Buffer类模拟Arraylist.get(), size(), add()
 * 主线程会不断轮询该缓冲区直到读取到数据为止。
 * 在buffer09中使用Lock来实现与synchronized一样的功能。
 */
public class MainThread09 {
	final public static Buffer09 buffer = new Buffer09();
	
	public static void main(String[] args) {
		MakeSomethingThread09 subThread = new MakeSomethingThread09();
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

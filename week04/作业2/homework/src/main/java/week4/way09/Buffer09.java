package week4.way09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer09 {
	private int size;
	private String[] array = new String[1];
	private Lock lock = new ReentrantLock();
	
	public int size() {
		try {
			lock.lock();
			return size;
		} finally {
			lock.unlock();
		}
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		array[size++] = value;
	}
}

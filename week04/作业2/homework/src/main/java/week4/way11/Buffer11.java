package week4.way11;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer11 {
	private int size;
	private String[] array = new String[1];
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public int size() {
		try {
			lock.readLock().lock();
			return size;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		array[size++] = value;
	}
}

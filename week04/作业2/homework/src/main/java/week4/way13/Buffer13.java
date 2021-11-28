package week4.way13;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer13 {
	private int size;
	private String[] array = new String[1];
	private Semaphore semaphore = new Semaphore(1);
	
	public int size() throws InterruptedException {
		try {
			semaphore.acquire();
			return size;
		} finally {
			semaphore.release();
		}
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		try {
			semaphore.acquire();
			array[size++] = value;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();
		}
	}
}

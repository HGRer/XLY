package week4.way12;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer12 {
	private int size;
	private String[] array = new String[1];
	
	public int size() {
		return size;
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		array[size++] = value;
	}
}

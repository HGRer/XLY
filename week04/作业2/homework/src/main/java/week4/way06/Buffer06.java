package week4.way06;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer06 {
	private int size;
	private String[] array = new String[1];
	
	public synchronized int size() {
		return size;
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		array[size++] = value;
	}
}

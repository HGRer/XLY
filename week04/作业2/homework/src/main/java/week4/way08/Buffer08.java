package week4.way08;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer08 {
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

package week4.way03;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer03 {
	private AtomicInteger size;
	private String[] array = new String[1];
	
	public int size() {
		return size.get();
	}
	
	public String get(int index) {
		return array[index];
	}
	
	public void add(String value) {
		array[size.getAndIncrement()] = value;
	}
}

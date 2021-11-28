package week4.way15;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟Arraylist.get(), size(), add()
 * @author hgggr
 *
 */
public class Buffer15 {
	private AtomicInteger size = new AtomicInteger(0);
	private String[] array = new String[2];
	
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

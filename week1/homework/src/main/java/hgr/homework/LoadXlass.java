package hgr.homework;

import java.io.InputStream;
import java.lang.reflect.Method;

public class LoadXlass extends ClassLoader {
	public static void main(String[] args) throws Exception {		
		try (InputStream is = LoadXlass.class.getClassLoader().getResourceAsStream("Hello.xlass");) {
			// load file
			if (is == null) {
				System.out.println("is -> null ");
				return;
			}
			byte[] byteArray = new byte[1024];
			int value = -1;
			int index = 0;
			while((value  = is.read()) != -1) {
				if (index < byteArray.length) {
					byteArray[index] = (byte) (255 - value);
					index++;
				} else {
					System.out.println("byteBuffer's capacity is too small");
					return;
				}
			}
			
			byte[] classByte = new byte[index];
			for (int i = 0; i < classByte.length; i++) {			
				classByte[i] = byteArray[i];
			}		
			// defind class
			Class<?> xClass = new LoadXlass().defineClass("Hello", classByte, 0, classByte.length);
			if (xClass == null) {
				System.out.println("xClass -> null");
			}
			// find method
			Method[] methods = xClass.getDeclaredMethods();
			Method helloMethod = null;
			for (Method m : methods) {
				if (m.getName().equals("hello")) {
					helloMethod = m;
					break;
				}
			}
			if (helloMethod == null) {
				System.out.println("helloMethod -> null");
				return;
			}
			if (helloMethod.getParameterCount() == 0) {
				helloMethod.invoke(xClass.newInstance());
			} else {
				System.out.println("helloMethod's ParameterCount > 0");
				// should do something
			}
		}

	}
}
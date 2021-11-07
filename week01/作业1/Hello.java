public class Hello {
	// 涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码
	public static void main(String[] args) {
		int[] intArray = new int[] {1, 2, 3} ;
		
		Hello hello = new Hello();
		hello.addition(intArray);
		
		Hello.subtraction(intArray);
		
		multiplication();
		
		Hello.division();
	}
	
	public void addition(int[] intArray) {
		int count = 0;
		for (int i : intArray) {
			count += i;
		}
		System.out.println("addition -> " + count);
	}
	
	public static void subtraction(int[] intArray) {
		if (intArray == null) {
			System.out.println("intArray -> null");
			return;
		}
		int origin = 20;
		for (int i = 0; i < intArray.length; i++) {
			origin -= intArray[i];
		}
		System.out.println("subtraction -> " + origin);
	}
	
	public static void multiplication() {
		int a = 10;
		int b = 256;
		System.out.println("multiplication -> " + (a * b));
	}
	
	public static void division() {
		Integer a = 10;
		Integer b = 300;
		System.out.println("division -> " + (b / a));
	}
}
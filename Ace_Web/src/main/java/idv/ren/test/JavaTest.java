package idv.ren.test;

import idv.ren.base.MyHashMap;

public class JavaTest {
	public static void main(String[] args) {
		MyHashMap map = new MyHashMap();
		
		map.put("S", "SS");
		
		MyHashMap map2 = new MyHashMap();
		map2.put(5, 2);
		
		MyHashMap map3 = new MyHashMap();
		map3.put("中文" , "J個key是中文的啦");
		
		System.out.println("First => " + map.get("s"));
		System.out.println("First => " + map2.get(5));
		System.out.println("Third => " + map3.get("中文"));
	}
}

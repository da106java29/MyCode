package GC_Reference_pak;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class Test_Gc {
	public static void main(String[] args) {
			
		//############ 沒有使用過 Start ############
		
		/*	強引用（StrongReference）
		 * 	強引用不會被GC回收，並且在java.lang.ref裡也沒有實際的對應型別。
		 * */
		Object obj1 = new Object();
		//這裡的obj引用便是一個強引用，不會被GC回收。
		
		/*	虛引用（PhantomReference）
		 * 	當GC一但發現了虛引用物件，將會將PhantomReference物件插入ReferenceQueue佇列，而此時PhantomReference所指向的物件並沒有被GC回收，
		 * 	而是要等到ReferenceQueue被你真正的處理後才會被回收。
		 */
		Object obj = new Object();
		ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
		PhantomReference<Object> phanRef = new PhantomReference<Object>(obj, refQueue);
		// 呼叫phanRef.get()不管在什麼情況下會一直返回null
		Object objg = phanRef.get();
		// 如果obj被置為null，當GC發現了虛引用，GC會將phanRef插入進我們之前建立時傳入的refQueue佇列
		// 注意，此時phanRef所引用的obj物件，並沒有被GC回收，在我們顯式地呼叫refQueue.poll返回phanRef之後
		// 當GC第二次發現虛引用，而此時JVM將phanRef插入到refQueue會插入失敗，此時GC才會對obj進行回收
		Reference<? extends Object> phanRefP = refQueue.poll();
		
		//############ 沒有使用過 End ############
		
		String aa = "AA";
		String bb = "BB";
		
		/*	弱引用（WeakReference）
		 * 	當GC一但發現了弱引用物件，將會釋放WeakReference所引用的物件。弱引用使用方法與軟引用類似，但回收策略不同。
		 * 	### 直接使用system.gc()的話，極高機率會直接被GC給回收掉。
		 * */
		WeakReference wrf = new WeakReference(aa);
		
		/*	軟引用（SoftReference）
		 * 	軟引用在JVM報告記憶體不足的時候才會被GC回收，否則不會回收，正是由於這種特性軟引用在caching和pooling中用處廣泛。
		 * 	### 即使使用system.gc()也不一定會被回收，應該就是可以強制讓某物件存在棧裡面，直到記憶體不足時，jvm會去把軟引用(SoftReference)的物件做回收。
		 * */
		
		SoftReference srf = new SoftReference(bb);

		System.out.println("First wrf => " + wrf.get());
		System.out.println("First srf => " + srf.get());
		
		aa = null;
		bb = null;
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("Thread is error!!");
			System.gc();
			
			System.out.println(wrf.get());
			System.out.println(srf.get());
		}
		System.gc();
		
		System.out.println("Second wrf => " + wrf.get());
		System.out.println("Second srf => " + srf.get());
		
//		wrf.clear();
//		srf.clear();
		
		try {
			System.gc();
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("Thread is error!!");
			System.gc();
			
			System.out.println(wrf.get());
			System.out.println(srf.get());
		}
		
		System.gc();
		
		System.out.println("Third wrf => " + wrf.get());
		System.out.println("Third srf => " + srf.get());
	}
}

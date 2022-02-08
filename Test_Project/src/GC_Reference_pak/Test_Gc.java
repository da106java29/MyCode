package GC_Reference_pak;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class Test_Gc {
	public static void main(String[] args) {
			
		//############ �S���ϥιL Start ############
		
		/*	�j�ޥΡ]StrongReference�^
		 * 	�j�ޥΤ��|�QGC�^���A�åB�bjava.lang.ref�̤]�S����ڪ��������O�C
		 * */
		Object obj1 = new Object();
		//�o�̪�obj�ޥΫK�O�@�ӱj�ޥΡA���|�QGC�^���C
		
		/*	��ޥΡ]PhantomReference�^
		 * 	��GC�@���o�{�F��ޥΪ���A�N�|�NPhantomReference���󴡤JReferenceQueue��C�A�Ӧ���PhantomReference�ҫ��V������èS���QGC�^���A
		 * 	�ӬO�n����ReferenceQueue�Q�A�u�����B�z��~�|�Q�^���C
		 */
		Object obj = new Object();
		ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
		PhantomReference<Object> phanRef = new PhantomReference<Object>(obj, refQueue);
		// �I�sphanRef.get()���ަb���򱡪p�U�|�@����^null
		Object objg = phanRef.get();
		// �p�Gobj�Q�m��null�A��GC�o�{�F��ޥΡAGC�|�NphanRef���J�i�ڭ̤��e�إ߮ɶǤJ��refQueue��C
		// �`�N�A����phanRef�ҤޥΪ�obj����A�èS���QGC�^���A�b�ڭ��㦡�a�I�srefQueue.poll��^phanRef����
		// ��GC�ĤG���o�{��ޥΡA�Ӧ���JVM�NphanRef���J��refQueue�|���J���ѡA����GC�~�|��obj�i��^��
		Reference<? extends Object> phanRefP = refQueue.poll();
		
		//############ �S���ϥιL End ############
		
		String aa = "AA";
		String bb = "BB";
		
		/*	�z�ޥΡ]WeakReference�^
		 * 	��GC�@���o�{�F�z�ޥΪ���A�N�|����WeakReference�ҤޥΪ�����C�z�ޥΨϥΤ�k�P�n�ޥ������A���^���������P�C
		 * 	### �����ϥ�system.gc()���ܡA�������v�|�����QGC���^�����C
		 * */
		WeakReference wrf = new WeakReference(aa);
		
		/*	�n�ޥΡ]SoftReference�^
		 * 	�n�ޥΦbJVM���i�O���餣�����ɭԤ~�|�QGC�^���A�_�h���|�^���A���O�ѩ�o�دS�ʳn�ޥΦbcaching�Mpooling���γB�s�x�C
		 * 	### �Y�Ϩϥ�system.gc()�]���@�w�|�Q�^���A���ӴN�O�i�H�j�����Y����s�b�̸̭��A����O���餣���ɡAjvm�|�h��n�ޥ�(SoftReference)�����󰵦^���C
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

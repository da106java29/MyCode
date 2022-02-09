package idv.ren.base;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyHashMap extends HashMap<Object, Object> {

	private static final long serialVersionUID = 1L;

	@Override
	public Object put(Object okey, Object value) {
		String nkey = null;
		
		if(okey instanceof String) {
			//§PÂ_¦r¦ê¬O§_§t¤¤¤å
			if(((String) okey).matches("[\\u4E00-\\u9FA5]+")) {
				nkey = (String) okey;
			}else {
				nkey = ((String) okey).toLowerCase();
			}
		}else{
			return super.put(okey, value);
		}
		return super.put(nkey, value);
	}
}

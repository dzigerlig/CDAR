package cdar.bll.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cdar.bll.entity.Locking;

public class LockingManager {
	private static Map<String, Locking> lockingMap = new HashMap<String, Locking>();
	private final String CONSUMER = "C";
	private final String PRODUCER = "P";

	private String generateKey(boolean isProducer, int treeId) {
		String key;
		if (isProducer) {
			key = PRODUCER + treeId;
		} else {
			key = CONSUMER + treeId;
		}
		return key;
	}
	
	public void renewLockingTime(boolean isProducer, int treeId, int userId) {
		Locking locking;
		String key = generateKey(isProducer, treeId);
		locking = lockingMap.get(key);
		if (locking == null) {
			locking = new Locking(userId, treeId, isProducer);
			lockingMap.put(key, locking);
		}
		locking.renewLockingTime();
	}


	public boolean isExpired(boolean isProducer, int treeId) {
		Locking locking;
		String key = generateKey(isProducer, treeId);
		locking = lockingMap.get(key);
		if (locking == null) {
			return true;
		}
		return locking.getLockingTime().before(new Date());
	}
}

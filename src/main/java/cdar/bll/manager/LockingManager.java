package cdar.bll.manager;

import java.util.HashMap;
import java.util.Map;

import cdar.bll.entity.Locking;
import cdar.bll.exceptions.LockingException;
import cdar.dal.user.UserRepository;

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
	
	public void lock(boolean isProducer, int treeId, int userId) throws LockingException {
		Locking locking;
		String key = generateKey(isProducer, treeId);
		locking = lockingMap.get(key);
		if(locking == null || locking.isExpired())
		{
			lockingMap.put(key, new Locking(userId,treeId,isProducer));
		}
		else if(locking.getUserId()==userId)
		{
			renewLockingTime(key, locking);
		}
		else{
			throw new LockingException();
		}
	}
	
	private void renewLockingTime(String key, Locking locking) throws LockingException {
		if(!lockingMap.containsKey(key)){
			throw new LockingException();
		}
		else{
			locking.renewLockingTime();
			lockingMap.put(key, locking);
		}
	}
	
	public String getLockText(boolean isProducer, int treeId){
		Locking locking = lockingMap.get(generateKey(isProducer, treeId));
			try {
				return String.format("Object is locked by User %s till %s", new UserRepository().getUser(locking.getUserId()).getUsername(), lockingMap.get(generateKey(isProducer, treeId)).getLockingTime().toString() );
			} catch (Exception e) {
				e.printStackTrace();
				return "Object is locked";
			} 
	}
}

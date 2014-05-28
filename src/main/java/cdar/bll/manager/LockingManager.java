package cdar.bll.manager;

import java.util.HashMap;
import java.util.Map;

import cdar.bll.entity.Locking;
import cdar.bll.exceptions.LockingException;
import cdar.dal.user.UserRepository;

/**
 * The Class LockingManager.
 */
public class LockingManager {
	
	/** The locking map. */
	private static Map<String, Locking> lockingMap = new HashMap<String, Locking>();
	
	/** The consumer. */
	private final String CONSUMER = "C";
	
	/** The producer. */
	private final String PRODUCER = "P";

	/**
	 * Generate key.
	 *
	 * @param isProducer the is producer
	 * @param treeId the tree id
	 * @return the string
	 */
	private String generateKey(boolean isProducer, int treeId) {
		String key;
		if (isProducer) {
			key = PRODUCER + treeId;
		} else {
			key = CONSUMER + treeId;
		}
		return key;
	}
	
	/**
	 * Lock.
	 *
	 * @param isProducer the is producer
	 * @param treeId the tree id
	 * @param userId the user id
	 * @throws LockingException the locking exception
	 */
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
	
	/**
	 * Renew locking time.
	 *
	 * @param key the key
	 * @param locking the locking
	 * @throws LockingException the locking exception
	 */
	private void renewLockingTime(String key, Locking locking) throws LockingException {
		if(!lockingMap.containsKey(key)){
			throw new LockingException();
		}
		else{
			locking.renewLockingTime();
			lockingMap.put(key, locking);
		}
	}
	
	/**
	 * Gets the lock text.
	 *
	 * @param isProducer the is producer
	 * @param treeId the tree id
	 * @return the lock text
	 */
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

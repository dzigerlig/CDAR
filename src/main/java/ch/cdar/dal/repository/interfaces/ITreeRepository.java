package ch.cdar.dal.repository.interfaces;

import java.util.List;

import ch.cdar.bll.entity.Tree;
import ch.cdar.dal.exception.CreationException;
import ch.cdar.dal.exception.EntityException;
import ch.cdar.dal.exception.UnknownProjectTreeException;
import ch.cdar.dal.exception.UnknownTreeException;
import ch.cdar.dal.exception.UnknownUserException;

/**
 * The Interface ITreeRepository.
 */
public interface ITreeRepository {
	/**
	 * Gets the trees.
	 *
	 * @param uid the uid
	 * @return the trees
	 * @throws UnknownUserException the unknown user exception
	 * @throws EntityException the entity exception
	 */
	public List<Tree> getTrees(int uid) throws UnknownUserException, EntityException;
	
	/**
	 * Gets the tree.
	 *
	 * @param treeId the tree id
	 * @return the tree
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Tree getTree(int treeId) throws UnknownTreeException, UnknownProjectTreeException, EntityException;
	
	/**
	 * Creates the tree.
	 *
	 * @param tree the tree
	 * @return the tree
	 * @throws CreationException the creation exception
	 * @throws EntityException the entity exception
	 */
	public Tree createTree(Tree tree) throws CreationException, EntityException;
	
	/**
	 * Update tree.
	 *
	 * @param tree the tree
	 * @return the tree
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public Tree updateTree(Tree tree) throws UnknownTreeException, UnknownProjectTreeException;
	
	/**
	 * Delete tree.
	 *
	 * @param treeId the tree id
	 * @throws UnknownTreeException the unknown tree exception
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 */
	public void deleteTree(int treeId) throws UnknownTreeException, UnknownProjectTreeException;
}

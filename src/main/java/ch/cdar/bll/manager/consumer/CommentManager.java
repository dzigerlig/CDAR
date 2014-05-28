package ch.cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import ch.cdar.bll.entity.consumer.Comment;
import ch.cdar.bll.entity.consumer.ProjectNode;
import ch.cdar.dal.consumer.CommentRepository;
import ch.cdar.dal.exceptions.CreationException;
import ch.cdar.dal.exceptions.EntityException;
import ch.cdar.dal.exceptions.UnknownCommentException;
import ch.cdar.dal.exceptions.UnknownProjectTreeException;

/**
 * The Class CommentManager.
 */
public class CommentManager {
	
	/** The cr. */
	private CommentRepository cr = new CommentRepository();
	
	/**
	 * Gets the comments.
	 *
	 * @param pnodeId the pnode id
	 * @return the comments
	 * @throws EntityException the entity exception
	 */
	public Set<Comment> getComments(int pnodeId) throws EntityException   {
		Set<Comment> comments = new HashSet<Comment>();
		
		for (Comment comment : cr.getComments(pnodeId)) {
			comments.add(comment);
		}
		
		return comments;
	}
	
	/**
	 * Gets the comments by tree.
	 *
	 * @param treeId the tree id
	 * @return the comments by tree
	 * @throws UnknownProjectTreeException the unknown project tree exception
	 * @throws EntityException the entity exception
	 */
	public Set<Comment> getCommentsByTree(int treeId) throws UnknownProjectTreeException, EntityException {
		Set<Comment> comments = new HashSet<Comment>();
		
		ProjectNodeManager pnm = new ProjectNodeManager();
		
		for (ProjectNode projectNode : pnm.getProjectNodes(treeId)) {
			comments.addAll(getComments(projectNode.getId()));
		}
		
		return comments;
	}
	
	/**
	 * Gets the comment.
	 *
	 * @param commentId the comment id
	 * @return the comment
	 * @throws UnknownCommentException the unknown comment exception
	 * @throws EntityException the entity exception
	 */
	public Comment getComment(int commentId) throws UnknownCommentException, EntityException {
		return cr.getComment(commentId);
	}
	
	/**
	 * Adds the comment.
	 *
	 * @param comment the comment
	 * @return the comment
	 * @throws CreationException the creation exception
	 */
	public Comment addComment(Comment comment) throws CreationException   {
		return cr.createComment(comment);
	}
	
	/**
	 * Delete comment.
	 *
	 * @param commentId the comment id
	 * @throws UnknownCommentException the unknown comment exception
	 */
	public void deleteComment(int commentId) throws UnknownCommentException   {
		cr.deleteComment(commentId);
	}
	
	/**
	 * Update comment.
	 *
	 * @param comment the comment
	 * @return the comment
	 * @throws UnknownCommentException the unknown comment exception
	 * @throws EntityException the entity exception
	 */
	public Comment updateComment(Comment comment) throws UnknownCommentException, EntityException  {
		Comment updatedComment = cr.getComment(comment.getId());
		if (comment.getComment()!=null) {
			updatedComment.setComment(comment.getComment());
		}
		return cr.updateComment(updatedComment);
	}
}

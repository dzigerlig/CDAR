package cdar.bll.manager.consumer;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.consumer.Comment;
import cdar.dal.consumer.CommentRepository;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownCommentException;

public class CommentManager {
	private CommentRepository cr = new CommentRepository();
	
	public Set<Comment> getComments(int pnodeId) throws EntityException   {
		Set<Comment> comments = new HashSet<Comment>();
		
		for (Comment comment : cr.getComments(pnodeId)) {
			comments.add(comment);
		}
		
		return comments;
	}
	
	public Comment getComment(int commentId) throws UnknownCommentException, EntityException {
		return cr.getComment(commentId);
	}
	
	public Comment addComment(Comment comment) throws CreationException   {
		return cr.createComment(comment);
	}
	
	public void deleteComment(int commentId) throws UnknownCommentException   {
		cr.deleteComment(commentId);
	}
	
	public Comment updateComment(Comment comment) throws UnknownCommentException, EntityException  {
		Comment updatedComment = cr.getComment(comment.getId());
		if (comment.getComment()!=null) {
			updatedComment.setComment(comment.getComment());
		}
		return cr.updateComment(updatedComment);
	}
}

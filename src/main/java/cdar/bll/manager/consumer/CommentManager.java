package cdar.bll.manager.consumer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.entity.consumer.Comment;
import cdar.dal.consumer.CommentRepository;
import cdar.dal.exceptions.UnknownCommentException;

public class CommentManager {
	private CommentRepository cr = new CommentRepository();
	
	public Set<Comment> getComments(int pnodeId) throws SQLException {
		Set<Comment> comments = new HashSet<Comment>();
		
		for (Comment comment : cr.getComments(pnodeId)) {
			comments.add(comment);
		}
		
		return comments;
	}
	
	public Comment getComment(int commentId) throws UnknownCommentException {
		return cr.getComment(commentId);
	}
	
	public Comment addComment(Comment comment) throws Exception {
		return cr.createComment(comment);
	}
	
	public void deleteComment(int commentId) throws Exception {
		cr.deleteComment(commentId);
	}
	
	public Comment updateComment(Comment comment) throws Exception {
		Comment updatedComment = cr.getComment(comment.getId());
		if (comment.getComment()!=null) {
			updatedComment.setComment(comment.getComment());
		}
		return cr.updateComment(updatedComment);
	}
}

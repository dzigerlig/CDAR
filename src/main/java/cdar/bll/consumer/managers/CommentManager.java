package cdar.bll.consumer.managers;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.Comment;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.persistence.jdbc.consumer.CommentRepository;

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
	
	public Comment addComment(int uid, int kpnid, String commentText) throws Exception {
		Comment comment = new Comment();
		comment.setRefUserId(uid);
		comment.setRefProjectNode(kpnid);
		comment.setComment(commentText);
		return cr.createComment(comment);
	}
	
	public boolean removeComment(int id) throws Exception {
		return cr.deleteComment(id);
	}
	
	public Comment updateComment(Comment comment) throws Exception {
		return cr.updateComment(comment);
	}
}

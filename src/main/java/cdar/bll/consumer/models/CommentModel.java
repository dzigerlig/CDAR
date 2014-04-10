package cdar.bll.consumer.models;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.consumer.Comment;
import cdar.dal.persistence.jdbc.consumer.CommentDao;
import cdar.dal.persistence.jdbc.consumer.ConsumerDaoController;

public class CommentModel {
	private ConsumerDaoController cdc = new ConsumerDaoController();
	
	public Set<Comment> getComments(int pnodeid) {
		Set<Comment> comments = new HashSet<Comment>();
		
		for (CommentDao uc : cdc.getComments(pnodeid)) {
			comments.add(new Comment(uc));
		}
		
		return comments;
	}
	
	public Comment getComment(int commentid) {
		return new Comment(cdc.getComment(commentid));
	}
	
	public Comment addComment(int uid, int kpnid, String comment) {
		CommentDao commentDao = new CommentDao(kpnid, uid, comment);
		return new Comment(commentDao.create());
	}
	
	public boolean deleteComment(int id) {
		CommentDao commentDao = cdc.getComment(id);
		return commentDao.delete();
	}
	
	public Comment updateComment(Comment comment) {
		CommentDao commentDao = cdc.getComment(comment.getId());
		commentDao.setComment(comment.getComment());
		return new Comment(commentDao.update());
	}
}

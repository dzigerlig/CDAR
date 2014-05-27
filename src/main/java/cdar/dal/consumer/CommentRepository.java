package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.consumer.Comment;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownCommentException;
import cdar.dal.helpers.DBConnection;
import cdar.dal.helpers.DBTableHelper;
import cdar.dal.helpers.DateHelper;

public class CommentRepository {

	public List<Comment> getComments(int nodeId) throws EntityException {
		String sql = String.format("SELECT COMMENT.ID, COMMENT.CREATION_TIME, COMMENT.LAST_MODIFICATION_TIME, COMMENT.UID, COMMENT.COMMENT, USER.USERNAME FROM %s AS COMMENT JOIN %s AS USER ON USER.ID = COMMENT.UID WHERE KPNID = ?",DBTableHelper.USERCOMMENT,DBTableHelper.USER);

		List<Comment> usercomments = new ArrayList<Comment>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, nodeId);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Comment usercomment = new Comment();
					usercomment.setId(result.getInt(1));
					usercomment.setCreationTime(DateHelper.getDate(result.getString(2)));
					usercomment.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					usercomment.setNodeId(nodeId);
					usercomment.setUserId(result.getInt(4));
					usercomment.setComment(result.getString(5));
					usercomment.setUsername(result.getString(6));
					usercomments.add(usercomment);
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new EntityException();
		}
		return usercomments;
	}
	
	public Comment getComment(int commentId) throws UnknownCommentException, EntityException {
		final String sql = String.format("SELECT COMMENT.ID, COMMENT.CREATION_TIME, COMMENT.LAST_MODIFICATION_TIME, COMMENT.KPNID, COMMENT.UID, COMMENT.COMMENT, USER.USERNAME FROM %s AS COMMENT JOIN %s AS USER ON USER.ID = COMMENT.UID WHERE COMMENT.ID = ?",DBTableHelper.USERCOMMENT,DBTableHelper.USER);

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, commentId);
			

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Comment usercomment = new Comment();
					usercomment.setId(result.getInt(1));
					usercomment.setCreationTime(DateHelper.getDate(result.getString(2)));
					usercomment.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					usercomment.setNodeId(result.getInt(4));
					usercomment.setUserId(result.getInt(5));
					usercomment.setComment(result.getString(6));
					usercomment.setUsername(result.getString(7));
					return usercomment;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownCommentException();
		}
		throw new UnknownCommentException();
	}
	
	public Comment createComment(Comment comment) throws CreationException {
		final String sql = String.format("INSERT INTO %s (CREATION_TIME, UID, KPNID, COMMENT) VALUES (?, ?, ?, ?)",DBTableHelper.USERCOMMENT);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, comment.getUserId());
			preparedStatement.setInt(3, comment.getNodeId());
			preparedStatement.setString(4, comment.getComment());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					comment.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CreationException();
		}
		return comment;
	}
	
	public Comment updateComment(Comment comment) throws UnknownCommentException  {
		final String sql = String.format("UPDATE %s SET LAST_MODIFICATION_TIME = ?, UID = ?, KPNID = ?, COMMENT = ?  WHERE id = ?",DBTableHelper.USERCOMMENT);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setString(1, DateHelper.getDate(new Date()));
			preparedStatement.setInt(2, comment.getUserId());
			preparedStatement.setInt(3, comment.getNodeId());
			preparedStatement.setString(4, comment.getComment());
			preparedStatement.setInt(5, comment.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw new UnknownCommentException();
		}
		return comment;
	}

	public void deleteComment(int commentId) throws UnknownCommentException {
		final String sql = String.format("DELETE FROM %s WHERE ID = ?",DBTableHelper.USERCOMMENT);
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, commentId);
			if (preparedStatement.executeUpdate()!=1) {
				throw new UnknownCommentException();
			}
		} catch (Exception ex) {
			throw new UnknownCommentException();
		}
	}
}

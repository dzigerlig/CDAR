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
import cdar.dal.DBConnection;
import cdar.dal.DateHelper;
import cdar.dal.exceptions.CreationException;
import cdar.dal.exceptions.EntityException;
import cdar.dal.exceptions.UnknownCommentException;

public class CommentRepository {

	public List<Comment> getComments(int kpnid) throws EntityException {
		String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, UID, COMMENT FROM USERCOMMENT WHERE KPNID = ?";

		List<Comment> usercomments = new ArrayList<Comment>();

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, kpnid);

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Comment usercomment = new Comment();
					usercomment.setId(result.getInt(1));
					usercomment.setCreationTime(DateHelper.getDate(result.getString(2)));
					usercomment.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					usercomment.setNodeId(kpnid);
					usercomment.setUserId(result.getInt(4));
					usercomment.setComment(result.getString(5));
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
	
	public Comment getComment(int id) throws UnknownCommentException, EntityException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, UID, COMMENT FROM USERCOMMENT WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Comment usercomment = new Comment();
					usercomment.setId(result.getInt(1));
					usercomment.setCreationTime(DateHelper.getDate(result.getString(2)));
					usercomment.setLastModificationTime(DateHelper.getDate(result.getString(3)));
					usercomment.setNodeId(result.getInt(4));
					usercomment.setUserId(result.getInt(5));
					usercomment.setComment(result.getString(6));
					return usercomment;
				}
			} catch (ParseException e) {
				throw new EntityException();
			}
		} catch (SQLException ex) {
			throw new UnknownCommentException();
		}
		throw new UnknownCommentException();
	}
	
	public Comment createComment(Comment comment) throws CreationException {
		final String sql = "INSERT INTO USERCOMMENT (CREATION_TIME, UID, KPNID, COMMENT) VALUES (?, ?, ?, ?)";
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
			throw new CreationException();
		}
		return comment;
	}
	
	public Comment updateComment(Comment comment) throws UnknownCommentException  {
		final String sql = "UPDATE USERCOMMENT SET LAST_MODIFICATION_TIME = ?, UID = ?, KPNID = ?, COMMENT = ?  WHERE id = ?";
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
		final String sql = "DELETE FROM USERCOMMENT WHERE ID = ?";
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

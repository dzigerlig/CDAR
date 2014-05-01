package cdar.dal.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdar.bll.entity.consumer.Comment;
import cdar.dal.DBConnection;
import cdar.dal.exceptions.UnknownCommentException;

public class CommentRepository {

	public List<Comment> getComments(int kpnid) throws SQLException {
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
					usercomment.setCreationTime(result.getDate(2));
					usercomment.setLastModificationTime(result.getDate(3));
					usercomment.setRefProjectNode(kpnid);
					usercomment.setRefUserId(result.getInt(4));
					usercomment.setComment(result.getString(5));
					usercomments.add(usercomment);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		}
		return usercomments;
	}
	
	public Comment getComment(int id) throws UnknownCommentException {
		final String sql = "SELECT ID, CREATION_TIME, LAST_MODIFICATION_TIME, KPNID, UID, COMMENT FROM USERCOMMENT WHERE ID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setInt(1, id);
			

			try (ResultSet result = preparedStatement.executeQuery()) {
				while (result.next()) {
					Comment usercomment = new Comment();
					usercomment.setId(result.getInt(1));
					usercomment.setCreationTime(result.getDate(2));
					usercomment.setLastModificationTime(result.getDate(3));
					usercomment.setRefProjectNode(4);
					usercomment.setRefUserId(result.getInt(5));
					usercomment.setComment(result.getString(6));
					return usercomment;
				}
			}
		} catch (SQLException ex) {
			throw new UnknownCommentException();
		}
		throw new UnknownCommentException();
	}
	
	public Comment createComment(Comment comment) throws Exception {
		final String sql = "INSERT INTO USERCOMMENT (CREATION_TIME, UID, KPNID, COMMENT) VALUES (?, ?, ?, ?)";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, comment.getRefUserId());
			preparedStatement.setInt(3, comment.getRefProjectNode());
			preparedStatement.setString(4, comment.getComment());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					comment.setId(generatedKeys.getInt(1));
				}
			}
		} catch (Exception ex) {
			throw ex;
		}
		return comment;
	}
	
	public Comment updateComment(Comment comment) throws Exception {
		final String sql = "UPDATE USERCOMMENT SET LAST_MODIFICATION_TIME = ?, UID = ?, KPNID = ?, COMMENT = ?  WHERE id = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql)) {
			preparedStatement.setDate(1, new java.sql.Date(new Date().getTime()));
			preparedStatement.setInt(2, comment.getRefUserId());
			preparedStatement.setInt(3, comment.getRefProjectNode());
			preparedStatement.setString(4, comment.getComment());
			preparedStatement.setInt(5, comment.getId());

			preparedStatement.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
		return comment;
	}

	public boolean deleteComment(int commentId) throws Exception {
		final String sql = "DELETE FROM USERCOMMENT WHERE ID = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, commentId);
			if (preparedStatement.executeUpdate()==1) {
				return true;
			} else {
				throw new UnknownCommentException();
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}

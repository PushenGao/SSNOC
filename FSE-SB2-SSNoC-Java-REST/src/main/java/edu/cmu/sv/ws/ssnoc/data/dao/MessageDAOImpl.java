package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;

public class MessageDAOImpl extends BaseDAOImpl implements IMessageDAO {
	/**
	 * This method will save the information of the message into the database.
	 *
	 * @param messagePO
	 *            - message information to be saved.
	 */
	@Override
	public void save(MessagePO messagePO) {
		Log.enter(messagePO);
		if (messagePO == null) {
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.INSERT_MESSAGE)) {
			stmt.setString(1, messagePO.getContent());
			stmt.setString(2, messagePO.getAuthor());
			stmt.setString(3, messagePO.getMessageType());
			stmt.setString(4, messagePO.getPostedAt());
			stmt.setString(5, messagePO.getTarget());

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}

	}

	/**
	 * This method with search for a message by his message id in the database.
	 * The search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param id
	 *            - message id to search for.
	 *
	 * @return - messagePO with the message information if a match is found.
	 */

	@Override
	public MessagePO findId(long id) {
		Log.enter(id);

		MessagePO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_MESSAGE_BY_ID)) {
			stmt.setLong(1, id);

			List<MessagePO> messagePOs = processResults(stmt);

			if (messagePOs.size() == 0) {
				Log.debug("No status crumb exists with crumbId = " + id);
			} else {
				po = messagePOs.get(0);
			}
		} catch (Exception e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	private List<MessagePO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		Log.debug("Executing stmt = " + stmt);
		List<MessagePO> messages = new ArrayList<MessagePO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				MessagePO po = new MessagePO();
				po.setMessageId(rs.getLong(1));
				po.setContent(rs.getString(2));
				po.setAuthor(rs.getString(3));
				po.setMessageType(rs.getString(4));
				po.setPostedAt(rs.getString(5));
				po.setTarget(rs.getString(6));
				messages.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(messages);
		}

		return messages;
	}

	/**
	 * This method with search for a message by his AUTHOR AND TARGETin the
	 * database. The search performed is a case insensitive search to allow case
	 * mismatch situations.
	 *
	 * @param AUTHOR
	 *            TARGET - User name to search for.
	 *
	 * @return - messagePO with the message information if a match is found.
	 */

	@Override
	public List<MessagePO> findByAT(String author, String target) {
		Log.enter(author);
		Log.enter(target);

		if (author == null || target == null) {
			return null;
		}
		List<MessagePO> messages = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_MESSAGE_BY_USERANDTARGET)) {
			stmt.setString(1, author);
			stmt.setString(2, target);
			stmt.setString(3, author);
			stmt.setString(4, target);

			messages = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;
	}

	/**
	 * This method with search for users whom a user has chatted with in the
	 * database. The search performed is a case insensitive search to allow case
	 * mismatch situations.
	 *
	 * @param AUTHOR
	 *            OR TARGET - User name to search for.
	 *
	 * @return - list of users
	 */
	@Override
	public List<MessagePO> findByAORT(String username) {
		Log.enter(username);

		if (username == null) {
			return null;
		}
		List<MessagePO> messages = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_MESSAGE_BY_USERORTARGET)) {
			stmt.setString(1, username);
			stmt.setString(2, username);

			messages = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;
	}

	/**
	 * This method with search for message which type is WALL
	 *
	 *
	 * @return - list of message pos
	 */

	@Override
	public List<MessagePO> findMessageByType(String messageType) {
		Log.enter("Message type: " + messageType);

		List<MessagePO> messages = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_MESSAGE_BY_MESSAGETYPE)) {
			stmt.setString(1, messageType);

			messages = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;
	}

	/**
	 * This method with get all message in a time period
	 *
	 *
	 * @return - list of message pos
	 */
	@Override
	public List<MessagePO> getPeriodMessage(String start, String end) {
		Log.enter("get all message in a time period");

		long startlong = Long.parseLong(start);
		long endlong = Long.parseLong(end);

		List<MessagePO> messages = new ArrayList<MessagePO>();
		List<MessagePO> messagesintime = new ArrayList<MessagePO>();

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.GET_ALL_MESSAGE)) {

			messages = processResults(stmt);

			for (MessagePO mpo : messages) {
				if (mpo.getMessageType().equals("CHAT")) {
					String posttime = mpo.getPostedAt();
					long posttimelong = Long.parseLong(posttime);
					if (posttimelong > startlong && posttimelong < endlong)
						messagesintime.add(mpo);
				}
			}
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messagesintime);
		}

		return messagesintime;
	}


	@Override
	public List<MessagePO> findMessageByTypeVisible(String messageType) {
		Log.enter("Message type: " + messageType);

		List<MessagePO> messages = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_MESSAGE_BY_MESSAGETYPE_WALL_ANNOUNCE_VISIBLE)) {
			stmt.setString(1, messageType);

			messages = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;
	}

}

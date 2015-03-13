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

public class SearchDAOImple extends BaseDAOImpl implements ISearchDAO {
	@Override
	public List<MessagePO> searchMessageOnWall(String keyword) {
		Log.enter(keyword);
		List<MessagePO> messagePOs = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.SEARCH_BY_KEYWORD_ON_WALL)) {
			stmt.setString(1, keyword);
			messagePOs = processMessageResults(stmt);
			Log.trace("search by skeyword on the wall " + keyword);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return messagePOs;
	}

	@Override
	public List<MessagePO> searchMessageChat(String username, String keyword) {
		Log.enter(keyword);
		Log.enter(username);
		List<MessagePO> messagePOs = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.SEARCH_BY_KEYWORD_ON_CHAT )) {
			stmt.setString(1, keyword);
			stmt.setString(2, username);
			stmt.setString(3, username);
			messagePOs = processMessageResults(stmt);
			Log.trace("search by skeyword on the wall " + keyword);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return messagePOs;
	}

		private List<MessagePO> processMessageResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

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

	@Override
	public List<MessagePO> searchMessageAnnouncement(String keyword) {
		Log.enter(keyword);
		List<MessagePO> messagePOs = new ArrayList<MessagePO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.SEARCH_BY_KEYWORD_ANNOUNCEMENT)) {
			stmt.setString(1, keyword);
			messagePOs = processMessageResults(stmt);
			Log.trace("search by skeyword on the wall " + keyword);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return messagePOs;
	}

}

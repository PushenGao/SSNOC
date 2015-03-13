package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;

public class CallLogDAOImpl extends BaseDAOImpl implements ICallLogDAO {

	@Override
	public List<String> getMostFrequentContacts(String user) {
		Log.enter(user);
		List<String> list = new ArrayList<String>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.MOST_FREQUENT_CONTACTS)) {
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			closeResultSet(rs);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(list);
		}
		return list;
	}

	@Override
	public void recordCall(String user1, String user2) {
		Log.enter(user1 + "," + user2);
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_CALL_LOG)) {
			stmt.setString(1, user1);
			stmt.setString(2, user2);
			executeStatement(stmt);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

}

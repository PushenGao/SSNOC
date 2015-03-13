package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * DAO implementation for saving User information in the H2 database.
 *
 */
public class UserDAOImpl extends BaseDAOImpl implements IUserDAO {
	/**
	 * This method will load users from the DB with specified account status. If
	 * no status information (null) is provided, it will load all users.
	 *
	 * @return - List of users
	 */
	@Override
	public List<UserPO> loadUsers() {
		Log.enter();

		String query = SQL.FIND_ALL_USERS;

		List<UserPO> users = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			users = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;
	}

	private List<UserPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<UserPO> users = new ArrayList<UserPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				UserPO po = new UserPO();
				po = new UserPO();
				po.setUserId(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setPassword(rs.getString(3));
				po.setSalt(rs.getString(4));
				po.setCreatedAt(rs.getString(5));
				po.setModifiedAt(rs.getString(6));
				po.setLastStatusCode(rs.getString(7));
				po.setStatus(rs.getString(8));
				po.setPrivilege(rs.getString(9));
				users.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

	/**
	 * This method with search for a user by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param userName
	 *            - User name to search for.
	 *
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public UserPO findByName(String userName) {
		Log.enter(userName);

		if (userName == null) {
			Log.warn("Inside findByName method with NULL userName.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_NAME)) {
			stmt.setString(1, userName.toUpperCase());

			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userName = " + userName);
			} else {
				po = users.get(0);
			}
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	/**
	 * This method will delete a user from the database
	 * @param userName
	 *            - User name to delete.
	 *
	 * @return - True is operation was successful, false otherwise
	 */
	@Override
	public Boolean deleteByName(String userName) {
		Log.enter(userName);

		if (userName == null) {
			Log.warn("Inside DeleteByName method with NULL userName.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.DELETE_USER)) {
			stmt.setString(1, userName);
			executeStatement(stmt);

		} catch (SQLException e) {
			Log.exit(po);
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * This method will save the information of the user into the database.
	 *
	 * @param userPO
	 *            - User information to be saved.
	 */
	@Override
	public void save(UserPO userPO) {
		Log.enter(userPO);
		if (userPO == null) {
			Log.warn("Inside save method with userPO == NULL");
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER)) {
			stmt.setString(1, userPO.getUserName());
			stmt.setString(2, userPO.getPassword());
			stmt.setString(3, userPO.getSalt());
			stmt.setString(4, userPO.getCreatedAt());
			stmt.setString(5, userPO.getCreatedAt());
			stmt.setString(6, userPO.getLastStatusCode());
			stmt.setString(7, userPO.getStatus());
			stmt.setString(8, userPO.getPrivilege());

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public void updateUser(UserPO userPO, String userName) {
		Log.enter(userPO);
		Log.enter(userName);

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USER)) {
			stmt.setString(1, userPO.getUserName());
			stmt.setString(2, userPO.getPassword());
			stmt.setString(3, userPO.getSalt());
			stmt.setString(4, userPO.getModifiedAt());
			stmt.setString(5, userPO.getStatus());
			stmt.setString(6, userPO.getPrivilege());
			stmt.setString(7, userName);

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public void updateUserStatus(String status, String userName) {
		Log.enter(status);
		Log.enter(userName);

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USER_LAST_STATUS)) {
			stmt.setString(1, status);
			stmt.setString(2, userName);
			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public List<UserPO> searchByName(String userName) {
		Log.enter(userName);
		List<UserPO> userPOs = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.SEARCH_BY_USERNAME)) {
			stmt.setString(1, userName);
			userPOs = processResults(stmt);
			Log.trace("search by " + userName);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return userPOs;
	}

	@Override

	public List<UserPO> searchByStatus(String status) {
		Log.enter(status);
		List<UserPO> userPOs = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.SEARCH_BY_STATUS)) {
			stmt.setString(1, status);
			userPOs = processResults(stmt);
			Log.trace("search by status " + status);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return userPOs;
	}

	@Override
	public List<UserPO> loadActiveUsers() {
		Log.enter();

		String query = SQL.FIND_ALL_ACTIVE_USERS;

		List<UserPO> users = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			users = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;

	}

}

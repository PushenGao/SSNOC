package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.PropertyUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 *
 */
public class DBUtils {
	private static List<String> CREATE_TABLE_LST;

	static {
		CREATE_TABLE_LST = new ArrayList<String>();
		CREATE_TABLE_LST.add(SQL.CREATE_USERS);
		CREATE_TABLE_LST.add(SQL.CREATE_STATUS_CRUMBS);
		CREATE_TABLE_LST.add(SQL.CREATE_MESSAGE);
		CREATE_TABLE_LST.add(SQL.CREATE_MEMORY_CRUMBS);
		CREATE_TABLE_LST.add(SQL.CREATE_PERFORMANCE_CRUMBS);
		CREATE_TABLE_LST.add(SQL.CREATE_CALL_LOGS);
	}
	/**
	 * This method will clear the database.
	 *
	 * @throws SQLException
	 */
	public static void clearDatabase() throws SQLException {
		Log.enter();
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();) {
			stmt.execute("DROP ALL OBJECTS DELETE FILES");
		}
		Log.exit();
	}

	/**
	 * This method will initialize the database.
	 *
	 * @throws SQLException
	 */
	public static void initializeDatabase() throws SQLException {
		boolean newDatabase = !doesTableExistInDB(DBUtils.getConnection(), SQL.SSN_USERS);
		createTablesInDB();
		if (newDatabase) {
			saveAdminUser();
		}
	}

	/**
	 * This method will save default admin user to the database.
	 */
	private static void saveAdminUser() {
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO po = new UserPO();
		po.setUserName("SSNAdmin");
		po.setPassword(PropertyUtils.ADMIN_CODE);
		po.setLastStatusCode("green");
		po.setStatus("active");
		po.setPrivilege("administrator");
		po.setCreatedAt("0");
		po.setModifiedAt("0");
		po = SSNCipher.encryptPassword(po);
		dao.save(po);
	}

	/**
	 * This method will create necessary tables in the database.
	 *
	 * @throws SQLException
	 */
	protected static void createTablesInDB() throws SQLException {
		Log.enter();
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();) {
			for (String query : CREATE_TABLE_LST) {
				Log.debug("Executing query: " + query);
				boolean status = stmt.execute(query);
				Log.debug("Query execution completed with status: " + status);
			}
			Log.info("Tables created successfully");
		}
		Log.exit();
	}

	/**
	 * This method will check if the table exists in the database.
	 *
	 * @param conn
	 *            - Connection to the database
	 * @param tableName
	 *            - Table name to check.
	 *
	 * @return - Flag whether the table exists or not.
	 *
	 * @throws SQLException
	 */
	public static boolean doesTableExistInDB(Connection conn, String tableName)
			throws SQLException {
		Log.enter(tableName);

		if (conn == null || tableName == null || "".equals(tableName.trim())) {
			Log.error("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}

		boolean tableExists = false;

		final String SELECT_QUERY = SQL.CHECK_TABLE_EXISTS_IN_DB;

		ResultSet rs = null;
		try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_QUERY)) {
			selectStmt.setString(1, tableName.toUpperCase());
			rs = selectStmt.executeQuery();
			int tableCount = 0;
			if (rs.next()) {
				tableCount = rs.getInt(1);
			}

			if (tableCount > 0) {
				tableExists = true;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		Log.exit(tableExists);

		return tableExists;
	}

	/**
	 * This method returns a database connection from the Hikari CP Connection
	 * Pool
	 *
	 * @return - Connection to the H2 database
	 *
	 * @throws SQLException
	 */
	public static final Connection getConnection() throws SQLException {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		return cp.getConnection();
	}

	public static final void setUsesTestDb(boolean usesTestDb) {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		cp.setUsesTestDb(usesTestDb);
	}

	public static final boolean isUsesTestDb() {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		return cp.isUsesTestDb();
	}

}

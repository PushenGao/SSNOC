package edu.cmu.sv.ws.ssnoc.data;

/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 *
 */
public class SQL {
	/*
	 * List the USERS, STATUS_CRUMB table name, and list all queries related to
	 * these table here.
	 */
	public static final String SSN_USERS = "SSN_USERS";
	public static final String SSN_STATUS_CRUMBS = "SSN_STATUS_CRUMBS";
	public static final String SSN_MESSAGE = "SSN_MESSAGE";
	public static final String SSN_MEMORY_CRUMBS = "SSN_MEMORY_CRUMBS";
	public static final String SSN_PERFORMANCE_CRUMBS = "SSN_PERFORMANCE_CRUMBS";
	public static final String SSN_CALL_LOGS = "SSN_CALL_LOGS";

	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
			+ " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
			+ " AND UPPER(TABLE_NAME) = UPPER(?)";

	// ****************************************************************
	// All queries related to USERS
	// ****************************************************************
	/**
	 * Query to create the USERS table.
	 */
	public static final String CREATE_USERS = "create table IF NOT EXISTS "
			+ SSN_USERS + " ( user_id IDENTITY PRIMARY KEY,"
			+ " user_name VARCHAR(100),"
			+ " password VARCHAR(512),"
			+ " salt VARCHAR(512),"
			+ " created_at VARCHAR(50),"
			+ " modified_at VARCHAR(50),"
			+ " last_status_code VARCHAR(10),"
			+ " status VARCHAR(10),"
			+ " privilege VARCHAR(20))";

	/**
	 * Query to load all users in the system.
	 */
	public static final String FIND_ALL_USERS = "select *"
			+ " from " + SSN_USERS + " order by user_name";

	/**
	 * Query to load all active users in the system.
	 */
	public static final String FIND_ALL_ACTIVE_USERS = "select *"
			+ " from " + SSN_USERS
			+ " where status = 'active'"
			+ " order by user_name";

	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_BY_NAME = "select *"
			+ " from "
			+ SSN_USERS
			+ " where UPPER(user_name) = UPPER(?)";

	/**
	 * Query to insert a row into the users table.
	 */
	public static final String INSERT_USER = "insert into " + SSN_USERS
			+ " (user_name, password, salt, created_at, modified_at, last_status_code, status, privilege)"
			+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Query to ...
	 */
	public static final String UPDATE_USER = "update " + SSN_USERS
			+ " set user_name = ?, password = ?, salt = ?, modified_at = ?, status = ?, privilege = ?"
			+ " where user_name = ?";

	/**
	 * Query to ...
	 */
	public static final String UPDATE_USER_LAST_STATUS = "update " + SSN_USERS
			+ " set last_status_code = ?"
			+ " where user_name = ?";

	/**
	 * Query to delete a row from the users table.
	 */
	public static final String DELETE_USER = "delete from " + SSN_USERS
			+ " where UPPER(user_name) = UPPER(?)";

	/**
	 * Query to clear the USERS table.
	 */
	public static final String CLEAR_USERS = "truncate table " + SSN_USERS;

	// ****************************************************************
	// All queries related to STATUS_CRUMB
	// ****************************************************************
	/**
	 * Query to create the STATUS_CRUMB table.
	 */
	public static final String CREATE_STATUS_CRUMBS = "create table IF NOT EXISTS "
			+ SSN_STATUS_CRUMBS + " ( crumb_id IDENTITY PRIMARY KEY,"
			+ " user_name VARCHAR(100),"
			+ " status_code VARCHAR(10),"
			+ " created_at VARCHAR(50) )";

	/**
	 * Query to insert a row into the STATUS_CRUMB table.
	 */
	public static final String INSERT_STATUS_CRUMB = "insert into " + SSN_STATUS_CRUMBS
			+ " (user_name, status_code, created_at)"
			+ " values (?, ?, ?)";

	/**
	 * Query to ...
	 */
	public static final String FIND_STATUS_CRUMB_BY_ID = "select *"
			+ " from "
			+ SSN_STATUS_CRUMBS
			+ " where crumb_id = ?";

	/**
	 * Query to ...
	 */
	public static final String FIND_STATUS_CRUMBS_BY_USER_NAME = "select *"
			+ " from "
			+ SSN_STATUS_CRUMBS
			+ " where UPPER(user_name) = UPPER(?)"
			+ " order by created_at";

	/**
	 * Query to clear the STATUS_CRUMBS table.
	 */
	public static final String CLEAR_STATUS_CRUMBS = "truncate table " + SSN_STATUS_CRUMBS;

	// ****************************************************************
	// All queries related to MESSAGE
	// ****************************************************************
	/**
	 * Query to create the MESSAGE table.
	 */
	public static final String CREATE_MESSAGE = "create table IF NOT EXISTS "
			+ SSN_MESSAGE + " ( message_id IDENTITY PRIMARY KEY,"//quite magic
			+ " content VARCHAR(1024)," + " author VARCHAR(100),"
			+ " messageType VARCHAR(16),"
			+ " postedAt VARCHAR(100),"
			+ " target VARCHAR(100))";

	/**
	 * Query to insert a row into the message table.
	 */
	public static final String INSERT_MESSAGE = "insert into " + SSN_MESSAGE
			+ " (content, author, messageType, postedAt, target) values (?, ?, ?, ?, ?)";

	/**
	* Query to find message by ID
	*/
	public static final String FIND_MESSAGE_BY_ID = "select *"
	+ " from "
	+ SSN_MESSAGE
	+ " where message_id = ?";

	/**
	* Query to find message by ID
	*/
	public static final String GET_ALL_MESSAGE = "select *"
	+ " from "
	+ SSN_MESSAGE;

	/**
	* Query to find the message by user and target
	*/
	public static final String FIND_MESSAGE_BY_USERANDTARGET = "select *"
	+ " from "
	+ SSN_MESSAGE
	+ " where (author = ? and target = ?)"
	+ "OR (target = ? and author = ?)";

	/**
	* Query to find message by author or target
	*/
	public static final String FIND_MESSAGE_BY_USERORTARGET = "select *"
		    + " from "
			+ SSN_MESSAGE
			+ " where (author = ?)"
			+ "OR (target = ?)";
	/**
	* Query to find message by message type
	*/
	public static final String FIND_MESSAGE_BY_MESSAGETYPE = "select *"
		    + " from "
			+ SSN_MESSAGE
			+ " where (messageType = ?)";

	/**
	 * Query to clear the MESSAGE table
	 */
	public static final String CLEAR_MESSAGE = "truncate table " + SSN_MESSAGE;

	public static final String DROP_MESSAGE =  "DROP TABLE SSN_MESSAGE IF EXISTS";

	// ****************************************************************
	// All queries related to MEMORY_CRUMBS
	// ****************************************************************
	public static final String CREATE_MEMORY_CRUMBS = "create table IF NOT EXISTS "
			+ SSN_MEMORY_CRUMBS + " ( crumb_id IDENTITY PRIMARY KEY,"
			+ " created_at VARCHAR(50),"
			+ " used_volatile BIGINT,"
			+ " remaining_volatile BIGINT,"
			+ " used_persistent BIGINT,"
			+ " remaining_persistent BIGINT)";

	public static final String INSERT_MEMORY_CRUMBS = "insert into " + SSN_MEMORY_CRUMBS
			+ " (created_at, used_volatile, remaining_volatile, used_persistent, remaining_persistent)"
			+ " values (?, ?, ?, ?, ?)";

	public static final String CLEAR_MEMORY_CRUMBS = "truncate table " + SSN_MEMORY_CRUMBS;

	public static final String FIND_MEMORY_CRUMBS_BY_HOURS = "select *"
		    + " from "
			+ SSN_MEMORY_CRUMBS
			+ " where (created_at > ?)";

	// ****************************************************************
	// All queries related to PERFORMANCE_CRUMBS
	// ****************************************************************
	public static final String CREATE_PERFORMANCE_CRUMBS = "create table IF NOT EXISTS "
			+ SSN_PERFORMANCE_CRUMBS + " ( crumb_id IDENTITY PRIMARY KEY,"
			+ " posts_per_second BIGINT,"
			+ " gets_per_second BIGINT)";

	public static final String INSERT_PERFORMANCE_CRUMBS = "insert into " + SSN_PERFORMANCE_CRUMBS
			+ " (posts_per_second, posts_per_second)"
			+ " values (?, ?)";

	public static final String CLEAR_PERFORMANCE_CRUMBS = "truncate table " + SSN_PERFORMANCE_CRUMBS;

	// ****************************************************************
		// All queries related to Search Information
		// ****************************************************************
	public static final String SEARCH_BY_USERNAME = "select * from " + SSN_USERS
			+ " where (user_name like ? )"
			+ "AND (status = 'active')"
			+ " order by user_name desc";

	public static final String SEARCH_BY_STATUS = "select * from " + SSN_USERS
			+ " where (last_status_code = ? )"
			+ "AND (status = 'active')"
			+ " order by user_name desc";

	public static final String SEARCH_BY_KEYWORD_ON_WALL = "select * from "
			+ SSN_MESSAGE + " join "+SSN_USERS
			+ " on SSN_USERS.USER_NAME = SSN_MESSAGE.AUTHOR"
			+ " where (content like ?  ) "
			+ "AND (messageType = 'WALL')"
			+ "AND (status='active')"
			+ "order by postedAt desc";

	public static final String SEARCH_BY_KEYWORD_ON_CHAT = "select * from "
			+ SSN_MESSAGE + " join SSN_USERS UA"
			+ " on UA.USER_NAME = SSN_MESSAGE.AUTHOR"
			+ " join SSN_USERS UT"
			+ " on UT.USER_NAME = SSN_MESSAGE.target"
			+ " where (content like ?  )"
			+ "AND (target = ? or author = ?) "
			+ "AND (messageType = 'CHAT')"
			+ "AND (UA.status='active' and UT.status='active')"
			+ "order by postedAt desc";

	public static final String SEARCH_BY_KEYWORD_ANNOUNCEMENT = "select * from "
			+ SSN_MESSAGE + " join "+SSN_USERS
			+ " on SSN_USERS.USER_NAME = SSN_MESSAGE.AUTHOR"
			+ " where (content like ?  ) "
			+ "AND (messageType = 'ANNOUNCEMENT')"
			+ "AND (status='active')"
			+ "order by postedAt desc";

	public static final String FIND_MESSAGE_BY_MESSAGETYPE_WALL_ANNOUNCE_VISIBLE = "select * from "
			+ SSN_MESSAGE + " join "+SSN_USERS
			+ " on SSN_USERS.USER_NAME = SSN_MESSAGE.AUTHOR "
			+ "AND (messageType = ?) "
			+ "AND (status='active')";

	// ****************************************************************
	// All queries related to CALL_LOGS
	// ****************************************************************
	public static final String CREATE_CALL_LOGS = "create table IF NOT EXISTS "
			+ SSN_CALL_LOGS + " ( call_id IDENTITY PRIMARY KEY,"
			+ " user1 VARCHAR(100),"
			+ " user2 VARCHAR(100))";

	public static final String INSERT_CALL_LOG = "insert into "
			+ SSN_CALL_LOGS
			+ " (user1, user2)"
			+ " values (?, ?)";

	public static final String MOST_FREQUENT_CONTACTS = "select user2, count(*) as cnt "
			+ "from " + SSN_CALL_LOGS
			+ " where (user1 = ?)"
			+ " group by user2"
			+ " order by cnt desc";
}



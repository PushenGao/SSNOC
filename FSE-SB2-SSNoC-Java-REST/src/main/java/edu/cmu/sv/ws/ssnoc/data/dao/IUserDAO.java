package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User information in the system.
 *
 */
public interface IUserDAO {
	/**
	 * This method will save the information of the user into the database.
	 *
	 * @param userPO
	 *            - User information to be saved.
	 */
	void save(UserPO userPO);

	/**
	 * This method will load all the users in the
	 * database.
	 *
	 * @return - List of all users.
	 */
	List<UserPO> loadUsers();

	List<UserPO> loadActiveUsers();

	/**
	 * This method will search for a user by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param userName
	 *            - User name to search for.
	 *
	 * @return - UserPO with the user information if a match is found.
	 */
	UserPO findByName(String userName);

	List<UserPO> searchByName(String userName);

	List<UserPO> searchByStatus(String status);

	void updateUser(UserPO userPO, String userName);

	void updateUserStatus(String status, String userName);

	/**
	 * This method will delete a user from the database.
	 *
	 * @param userName
	 *            - User name to delete.
	 *
	 * @return - Boolean value, true if the operation was successful
	 */
	Boolean deleteByName(String userName);
}

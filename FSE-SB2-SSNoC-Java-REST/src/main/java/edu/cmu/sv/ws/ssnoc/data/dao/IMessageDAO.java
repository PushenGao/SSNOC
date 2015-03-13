package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of Message information in the system.
 *
 */
public interface IMessageDAO {
	/**
	 * This method will save the information of the message into the database.
	 *
	 * @param messagePO
	 *            - message information to be saved.
	 */
	void save(MessagePO messagePO);


	/**
	 * This method with search for a message by his message id in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param id
	 *            - message id to search for.
	 *
	 * @return - messagePO with the message information if a match is found.
	 */
	MessagePO findId(long id);

	/**
	 * This method with search for a message by his AUTHOR AND TARGETin the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param AUTHOR TARGET
	 *            - User name to search for.
	 *
	 * @return - messagePOs.
	 */
	List<MessagePO> findByAT(String author, String target );

	/**
	 * This method with search for users whom a user has chatted with in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 *
	 * @param AUTHOR OR TARGET
	 *            - User name to search for.
	 *
	 * @return - list of message pos
	 */
	List<MessagePO> findByAORT(String username );
	/**
	 * This method searches for messages by type
	 *
	 *
	 * @return - list of message pos
	 */
	List<MessagePO> findMessageByType(String messageType);

	/**
	 * This method with get all message in a time period
	 *
	 *
	 * @return - list of message pos
	 */
	List<MessagePO> getPeriodMessage(String start, String end);

	List<MessagePO> findMessageByTypeVisible(String messageType);

}
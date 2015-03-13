package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

public interface ICallLogDAO {

	List<String> getMostFrequentContacts(String user);

	void recordCall(String user1, String user2);

}

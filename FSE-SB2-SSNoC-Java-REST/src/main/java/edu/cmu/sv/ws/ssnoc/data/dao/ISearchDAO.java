package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;

public interface ISearchDAO {
	List<MessagePO> searchMessageOnWall(String keyword);

	List<MessagePO> searchMessageChat(String username, String keyword);

	List<MessagePO> searchMessageAnnouncement(String keyword);
}

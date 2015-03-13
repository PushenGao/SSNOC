package edu.cmu.sv.ws.ssnoc.test;

import static org.junit.Assert.assertEquals;

import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class MessageDaoTest {

	DAOFactory dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUtils.setUsesTestDb(true);
		DBUtils.clearDatabase();
		DBUtils.initializeDatabase();
	}

	@Before
	public void setUp() throws Exception {
		Statement stmt = DBUtils.getConnection().createStatement();
		stmt.execute(SQL.CLEAR_MESSAGE);
		dao = DAOFactory.getInstance();
	}

	@Test
	public void testWriteMessage() {
		MessagePO messagepo = new MessagePO();
		messagepo.setContent("Hey How are you?");
		messagepo.setMessageType("WALL");
		messagepo.setAuthor("joe");
		dao.getMessageDAO().save(messagepo);
		assertEquals(1, dao.getMessageDAO().findMessageByType("WALL").size());
	}

	@Test
	public void testMessagebyID() {
		MessagePO messagepo = new MessagePO();
		messagepo.setContent("Need Help");
		messagepo.setMessageType("WALL");
		messagepo.setAuthor("joe");
		dao.getMessageDAO().save(messagepo);
		long id = dao.getMessageDAO().findMessageByType("WALL").get(0).getMessageId();
		assertEquals(id, dao.getMessageDAO().findId(id).getMessageId());
	}

	@Test
	public void testMessagebyAuthor() {
		MessagePO messagepo = new MessagePO();
		messagepo.setContent("Need Help");
		messagepo.setMessageType("WALL");
		messagepo.setAuthor("joe");
		dao.getMessageDAO().save(messagepo);
		assertEquals("joe", dao.getMessageDAO().findByAORT("joe").get(0)
				.getAuthor());
	}

	@Test
	public void testMessagebyAuthorAndTarget() {
		MessagePO messagepo = new MessagePO();
		messagepo.setContent("Need Help");
		messagepo.setMessageType("CHAT");
		messagepo.setAuthor("alice");
		messagepo.setTarget("bob");
		dao.getMessageDAO().save(messagepo);
		assertEquals(1, dao.getMessageDAO().findByAT("alice", "bob").size());
	}

	@Test
	public void testPeriodMessageInRange() {
		MessagePO po = new MessagePO();
		po.setContent("Need help");
		po.setPostedAt("0");
		po.setMessageType("CHAT");
		dao.getMessageDAO().save(po);
		po.setContent("Need help");
		po.setPostedAt("100");
		po.setMessageType("CHAT");
		dao.getMessageDAO().save(po);
		po.setContent("Need help");
		po.setPostedAt("200");
		po.setMessageType("CHAT");
		dao.getMessageDAO().save(po);
		MessagePO wallPo = new MessagePO();
		wallPo.setContent("Hello world");
		wallPo.setPostedAt("0");
		wallPo.setMessageType("WALL");
		dao.getMessageDAO().save(wallPo);
		assertEquals(1, dao.getMessageDAO().getPeriodMessage("50", "150").size());
	}

	@Test
	public void testPeriodMessageOutOfRange() {
		MessagePO po = new MessagePO();
		po.setContent("Need help");
		po.setPostedAt("1413432255044");
		po.setMessageType("CHAT");
		dao.getMessageDAO().save(po);
		assertEquals(0, dao.getMessageDAO().getPeriodMessage("0", "0").size());
	}

	@Test
	public void testVisbleMessages(){
		MessagePO po = new MessagePO();
		po.setMessageType("WALL");
		po.setContent("hey I am ok");
		dao.getMessageDAO().save(po);
		assertEquals(0, dao.getMessageDAO().findMessageByTypeVisible("WALL").size());


	}

	@Test
	public void testNulls() {
		IMessageDAO msgDao = dao.getMessageDAO();
		msgDao.findByAORT(null);
		msgDao.findByAT(null, null);
		msgDao.findByAT(null, "");
		msgDao.findByAT("", null);
		msgDao.findByAT("", "");
		msgDao.findMessageByType(null);
		msgDao.findMessageByTypeVisible(null);
		msgDao.save(null);
	}


}





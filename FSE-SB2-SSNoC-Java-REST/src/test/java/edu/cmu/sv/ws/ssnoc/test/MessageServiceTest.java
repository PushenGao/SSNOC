package edu.cmu.sv.ws.ssnoc.test;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessageService;
import edu.cmu.sv.ws.ssnoc.rest.MessagesService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;
import edu.cmu.sv.ws.ssnoc.rest.UsersService;

public class MessageServiceTest {

	User UserA = new User();
	User UserB = new User();
	MessageService ms = new MessageService();
	MessagesService mss = new MessagesService();
	UsersService uss = new UsersService();

	public Response response;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUtils.setUsesTestDb(true);
		DBUtils.clearDatabase();
		DBUtils.initializeDatabase();
	}

	@Before
	public void setUp() throws Exception {
		Statement stmt = DBUtils.getConnection().createStatement();
		stmt.execute(SQL.CLEAR_USERS);
		stmt.execute(SQL.DROP_MESSAGE);
		stmt.execute(SQL.CREATE_MESSAGE);
		UserService us = new UserService();
		UserA.setUserName("hill");
		UserA.setPassword("hill");
		UserB.setUserName("alex");
		UserB.setPassword("alex");
		us.addUser(UserA);
		us.addUser(UserB);
		System.out.println();
	}

	// POST A MESSAGE ON THE WALL
	@Test
	public void testPostMessageOnTheWall() {

		Message mpo = new Message();
		mpo.setContent("Hello Everybody");
		mpo.setPostedAt("20141009");
		response = ms.sendMessageWall("hill", mpo);
		Message message = (Message) response.getEntity();
		// MessagePO message = new MessagePO();
		// message = ConverterUtils.convert(a);
		Assert.assertTrue(message.getAuthor().equals("hill"));
		Assert.assertTrue(message.getContent().equals("Hello Everybody"));
		Assert.assertTrue(message.getPostedAt().equals("20141009"));
		Assert.assertTrue(message.getMessageType().equals("WALL"));
	}

	// THE USER WHO POST MESSAGE IS NOT EXIST
	@Test
	public void testNobodyPostMessageOnTheWall() {
		Message mpo = new Message();
		mpo.setContent("Hello Everybody");
		mpo.setPostedAt("20141009");
		response = ms.sendMessageWall("nobody", mpo);
		Assert.assertEquals(404, response.getStatus());
	}

	// GET ALL MESSAGES FROM THE WALL
	@Test
	public void testGetMessageFromTheWall() {

		Message mpo1 = new Message();
		List<Message> messages = new ArrayList<Message>();
		mpo1.setContent("Hello Everybody");
		mpo1.setPostedAt("20141009");
		Message mpo2 = new Message();
		mpo2.setContent("Hello Everybody");
		mpo2.setPostedAt("20141009");
		ms.sendMessageWall("hill", mpo1);
		ms.sendMessageWall("alex", mpo2);

		messages = mss.loadWALL();

		Assert.assertTrue(messages.get(0).getAuthor().equals("hill"));
		Assert.assertTrue(messages.get(0).getContent()
				.equals("Hello Everybody"));
		Assert.assertTrue(messages.get(0).getPostedAt().equals("20141009"));
		Assert.assertTrue(messages.get(0).getMessageType().equals("WALL"));

		Assert.assertTrue(messages.get(1).getAuthor().equals("alex"));
		Assert.assertTrue(messages.get(1).getContent()
				.equals("Hello Everybody"));
		Assert.assertTrue(messages.get(1).getPostedAt().equals("20141009"));
		Assert.assertTrue(messages.get(1).getMessageType().equals("WALL"));

		Assert.assertEquals(messages.size(), mss.loadWALLVisible().size());
	}

	// SEND A MESSAGE FROM A TO B
	@Test
	public void testSendMessageToAnother() {

		Message mpo = new Message();
		mpo.setContent("Hello alex");
		mpo.setPostedAt("20141009");
		response = ms.sendMessageChat("hill", "alex", mpo);
		Message message = (Message) response.getEntity();
		// MessagePO message = new MessagePO();
		// message = ConverterUtils.convert(a);
		Assert.assertTrue(message.getAuthor().equals("hill"));
		Assert.assertTrue(message.getContent().equals("Hello alex"));
		Assert.assertTrue(message.getPostedAt().equals("20141009"));
		Assert.assertTrue(message.getMessageType().equals("CHAT"));
		Assert.assertTrue(message.getTarget().equals("alex"));
	}

	// A SEND A MESSAGE TO NOBODY
	@Test
	public void testSendMessageToNobody() {
		Message mpo = new Message();
		mpo.setContent("Hello alex");
		mpo.setPostedAt("20141009");
		response = ms.sendMessageChat("hill", "nobody", mpo);
		Assert.assertEquals(404, response.getStatus());
	}

	// GET ALL MESSAGES BETWEEN USERS
	@Test
	public void testGetMessageBetweenUsers() {

		Message mpo1 = new Message();
		List<Message> messages = new ArrayList<Message>();
		mpo1.setContent("Hello alex");
		mpo1.setPostedAt("20141009");
		Message mpo2 = new Message();
		mpo2.setContent("Hello hill");
		mpo2.setPostedAt("20141009");
		ms.sendMessageChat("hill", "alex", mpo1);
		ms.sendMessageChat("alex", "hill", mpo2);
		messages = mss.loadChat("hill", "alex");

		Assert.assertTrue(messages.get(0).getAuthor().equals("hill"));
		Assert.assertTrue(messages.get(0).getContent().equals("Hello alex"));
		Assert.assertTrue(messages.get(0).getPostedAt().equals("20141009"));
		Assert.assertTrue(messages.get(0).getMessageType().equals("CHAT"));
		Assert.assertTrue(messages.get(0).getTarget().equals("alex"));

		Assert.assertTrue(messages.get(1).getAuthor().equals("alex"));
		Assert.assertTrue(messages.get(1).getContent().equals("Hello hill"));
		Assert.assertTrue(messages.get(1).getPostedAt().equals("20141009"));
		Assert.assertTrue(messages.get(1).getMessageType().equals("CHAT"));
		Assert.assertTrue(messages.get(1).getTarget().equals("hill"));

	}

	// GET MESSAGE BY ID
	@Test
	public void testGetMessageById() {
		Message mpo = new Message();
		mpo.setContent("Hello alex");
		mpo.setPostedAt("20141009");
		ms.sendMessageChat("hill", "alex", mpo);
		Message message = ms.loadMessage(1);

		Assert.assertTrue(message.getAuthor().equals("hill"));
		Assert.assertTrue(message.getContent().equals("Hello alex"));
		Assert.assertTrue(message.getPostedAt().equals("20141009"));
		Assert.assertTrue(message.getMessageType().equals("CHAT"));
		Assert.assertTrue(message.getTarget().equals("alex"));
	}

	// GET ALL CHATBODIES
	@Test
	public void testGetChatbuddies() {

		Message mpo1 = new Message();
		List<User> users = new ArrayList<User>();
		mpo1.setContent("Hello alex");
		mpo1.setPostedAt("20141009");
		ms.sendMessageChat("hill", "alex", mpo1);
		users = uss.loadChat("hill");

		Assert.assertTrue(users.get(0).getUserName().equals("alex"));
	}

	@Test
	public void testAnnouncement() {
	    Message mpo1 = new Message();
        mpo1.setContent("Hello everyone");
        mpo1.setPostedAt("20141009");
        mpo1.setAuthor("alex");
	    ms.sendMessageWall("announcement", mpo1);
	    Assert.assertTrue(mss.loadAnnouncements().get(0).getContent().equals("Hello everyone"));
	}

	@Test
	public void testAnnouncementVisible() {
	    Message mpo1 = new Message();
        mpo1.setContent("Hello everyone");
        mpo1.setPostedAt("20141009");
        mpo1.setAuthor("alex");
	    ms.sendMessageWall("announcement", mpo1);
	    Assert.assertTrue(mss.loadAnnouncementVisible().get(0).getContent().equals("Hello everyone"));
	}

	@Test
	public void testLoadChatVisible() {
		Message mpo1 = new Message();
		List<Message> msgs = new ArrayList<Message>();
		mpo1.setContent("Hello alex");
		mpo1.setPostedAt("20141009");
		ms.sendMessageChat("hill", "alex", mpo1);
		msgs = mss.loadChatVisible("hill", "alex");
		Assert.assertEquals(msgs.size(), 1);
	}


}

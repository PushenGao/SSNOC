package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertNotFound;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;


@RunWith(HttpJUnitRunner.class)
public class MessageServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");

	@Context
	public Response response;

	/*
	 * Create two persistent users which has been already tested- used for other tests
	 */
	@HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
				content = "{\"userName\":\"UserA\",\"password\":\"Kiran\"}" , order = 1)
		public void setUserA() {
			Assert.assertTrue((response.getStatus() == 200) || (response.getStatus() == 201) );
		}
	@HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"UserB\",\"password\":\"Kiran\"}" , order = 2)
	public void setUserB() {
		Assert.assertTrue((response.getStatus() == 200) || (response.getStatus() == 201));
	}

	/*
	 * Test method post a message on the wall, expected 201
	 */
	@HttpTest(method = Method.POST, path = "/message/UserA", type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"Hello Everybody\",\"postedAt\":\"20141007\"}" , order = 3)
	public void testWallMessagePostA() {
		Assert.assertTrue((response.getStatus() == 201));
	}
	//this one do not need to be tested, just to post a message
	@HttpTest(method = Method.POST, path = "/message/UserB", type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"Hello Everybody\",\"postedAt\":\"20141007\"}" , order = 4)
	public void testWallMessagePostB() {
		String a = response.getBody();
		MessagePO mpo = new MessagePO();
		mpo = ConverterUtils.convert(a);
		Assert.assertTrue(mpo.getAuthor().equals("UserB"));
		Assert.assertTrue(mpo.getContent().equals("Hello Everybody"));
		Assert.assertTrue(mpo.getPostedAt().equals("20141007"));
		Assert.assertTrue(mpo.getMessageType().equals("WALL"));
	}
	/*
	 * Test post message to NonExisted - expect 404
	 */
	@HttpTest(method = Method.POST, path = "/message/NonExisted", type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"Hello Everybody\",\"postedAt\":\"20141007\"}" , order = 4)
	public void testWallMessageNonExisted() {
		Assert.assertEquals(404, response.getStatus());
	}

	/*
	 * Test Get messages from the wall - expects 200
	 */
	@HttpTest(method = Method.GET, path = "/messages/wall" , order = 5)
	public void testUserMessageWall() {
		//Assert.assertTrue((response.getStatus() == 200));
		String a = response.getBody();
		System.out.println(a);
		List<MessagePO> mpoList = new ArrayList<MessagePO>();
		String[] subStringSet = a.substring(1,a.length()-1).split("},");
		for(String subString : subStringSet){
			MessagePO mpo = new MessagePO();
			mpo = ConverterUtils.convert(subString);
			mpoList.add(mpo);
		}
		Assert.assertTrue(mpoList.get(0).getAuthor().equals("UserA"));
		Assert.assertTrue(mpoList.get(0).getContent().equals("Hello Everybody"));
		Assert.assertTrue(mpoList.get(0).getPostedAt().equals("20141007"));
		Assert.assertTrue(mpoList.get(0).getMessageType().equals("WALL"));

		Assert.assertTrue(mpoList.get(1).getAuthor().equals("UserB"));
		Assert.assertTrue(mpoList.get(1).getContent().equals("Hello Everybody"));
		Assert.assertTrue(mpoList.get(1).getPostedAt().equals("20141007"));
		Assert.assertTrue(mpoList.get(1).getMessageType().equals("WALL"));
	}

	/*
	 * Test message from users A to B, expected 201
	 */
	@HttpTest(method = Method.POST, path = "/message/UserA/UserB", type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"Hello B\",\"postedAt\":\"20141008\"}", order = 6)
	public void testChatMessagePostA() {
		String a = response.getBody();
		MessagePO mpo = new MessagePO();
		mpo = ConverterUtils.convert(a);
		Assert.assertTrue(mpo.getAuthor().equals("UserA"));
		Assert.assertTrue(mpo.getContent().equals("Hello B"));
		Assert.assertTrue(mpo.getPostedAt().equals("20141008"));
		Assert.assertTrue(mpo.getMessageType().equals("CHAT"));
		Assert.assertTrue(mpo.getTarget().equals("UserB"));
	}

	/*
	 * Create a message between users B to A- expected 201
	 */
	@HttpTest(method = Method.POST, path = "/message/UserB/UserA", type = MediaType.APPLICATION_JSON, content = "{\"content\":\"Hello A\",\"postedAt\":\"20141008\"}", order = 7)
	public void testChatMessagePostB() {
		Assert.assertTrue(response.getStatus() == 201);
	}
	/*
	 * Test message from users A to NotExisted, expected 404
	 */
	@HttpTest(method = Method.POST, path = "/message/UserA/NotExisted", type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"Hello B\",\"postedAt\":\"20141008\"}", order = 8)
	public void testChatMessagePostWrong() {
		assertNotFound(response);
	}

	/*
	 * Test Get messages between A and NotExisted - expects empty set
	 */
	@HttpTest(method = Method.GET, path = "/messages/UserA/NotExisted", order = 9)
	public void testUserAMessageChatNotFound() {
		Assert.assertEquals("[]", response.getBody());

	}

	/*
	 * Test Get messages between two users
	 */
	@HttpTest(method = Method.GET, path = "/messages/UserA/UserB", order = 10)
	public void testUserMessageChat() {
		String a = response.getBody();
		System.out.println(a);
		List<MessagePO> mpoList = new ArrayList<MessagePO>();
		String[] subStringSet = a.substring(1,a.length()-1).split("},");
		for(String subString : subStringSet){
			MessagePO mpo = new MessagePO();
			mpo = ConverterUtils.convert(subString);
			mpoList.add(mpo);
		}
		Assert.assertTrue(mpoList.get(0).getAuthor().equals("UserA"));
		Assert.assertTrue(mpoList.get(0).getContent().equals("Hello B"));
		Assert.assertTrue(mpoList.get(0).getPostedAt().equals("20141008"));
		Assert.assertTrue(mpoList.get(0).getMessageType().equals("CHAT"));
		Assert.assertTrue(mpoList.get(0).getTarget().equals("UserB"));

		Assert.assertTrue(mpoList.get(1).getAuthor().equals("UserB"));
		Assert.assertTrue(mpoList.get(1).getContent().equals("Hello A"));
		Assert.assertTrue(mpoList.get(1).getPostedAt().equals("20141008"));
		Assert.assertTrue(mpoList.get(1).getMessageType().equals("CHAT"));
		Assert.assertTrue(mpoList.get(1).getTarget().equals("UserA"));
	}

	/*
	 * Test Get people who a user has chatted with - expects 200
	 */
	@HttpTest(method = Method.GET, path = "/users/UserA/chatbuddies", order = 11)
	public void testUserMessageChatbuddies() {
		String a = response.getBody();
		System.out.println(a);
		List<UserPO> upoList = new ArrayList<UserPO>();
		String[] subStringSet = a.substring(1,a.length()-1).split("},");
		for(String subString : subStringSet){
			UserPO upo = new UserPO();
			upo = ConverterUtils.stringConvertToUserPO(subString);
			upoList.add(upo);
		}
		Assert.assertTrue(upoList.get(0).getUserName().equals("UserB"));
	}

	/*
	 * Test Get people who a user has chatted with - expects empty set
	 */
	@HttpTest(method = Method.GET, path = "/users/NonExisted/chatbuddies", order = 11)
	public void testUserMessageChatbuddiesSad() {
		Assert.assertEquals("[]", response.getBody());
	}

	/*
	 * Test Get message using ID - expects 200
	 */
	@HttpTest(method = Method.GET, path = "/message/2", order = 12)
	public void testUserMessageId() {
		String a = response.getBody();
		MessagePO mpo = new MessagePO();
		mpo = ConverterUtils.convert(a);
		Assert.assertTrue(mpo.getAuthor().equals("UserB"));
		Assert.assertTrue(mpo.getContent().equals("Hello Everybody"));
		Assert.assertTrue(mpo.getPostedAt().equals("20141007"));
		Assert.assertTrue(mpo.getMessageType().equals("WALL"));
	}
	/*
	 * Test Get message using ID - expects 204
	 */
	@HttpTest(method = Method.GET, path = "/message/99", order = 13)
	public void testUserMessageIdNotFound() {
		Assert.assertEquals(204, response.getStatus());
	}
}

package edu.cmu.sv.ws.ssnoc.test;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessageService;
import edu.cmu.sv.ws.ssnoc.rest.SNAService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;

public class SNATest {

	User UserA = new User();
	User UserB = new User();
	User UserC = new User();
	User UserD = new User();
	MessageService ms = new MessageService();
	SNAService ss = new SNAService();
	List<List<String>> list = new ArrayList<List<String>>();

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
		stmt.execute(SQL.CLEAR_MESSAGE);
		UserService us = new UserService();
		UserA.setUserName("UserA");
		UserA.setPassword("UserA");
		UserA.setCreatedAt("0");
		UserB.setUserName("UserB");
		UserB.setPassword("UserB");
		UserB.setCreatedAt("0");
		UserC.setUserName("UserC");
		UserC.setPassword("UserC");
		UserC.setCreatedAt("0");
		UserD.setUserName("UserD");
		UserD.setPassword("UserD");
		UserD.setCreatedAt("0");
		us.addUser(UserA);
		us.addUser(UserB);
		us.addUser(UserC);
		us.addUser(UserD);
	}

	// Have 2 messages: B-D, C-D;
	// expected clusters like(A,B,C);(A,D)
	@Test
	public void testClusterMethod() {
		Message m1 = new Message();
		Message m2 = new Message();
		m1.setContent("UserB said hello to UserD");
		m1.setPostedAt("1413431166752");
		m2.setContent("UserC said hello to UserD");
		m2.setPostedAt("1413431202296");
		ms.sendMessageChat("UserB", "UserD", m1);
		ms.sendMessageChat("UserC", "UserD", m2);
		ss.cluster("1413000000000", "1414000000000", list);
		Collections.sort(list, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> o1, List<String> o2) {
				return Integer.compare(o2.size(), o1.size());
			}
		});
		List<String> cluster0 = list.get(0);
		List<String> cluster1 = list.get(1);
		Assert.assertEquals(3, cluster0.size());
		Assert.assertTrue(cluster0.contains("UserA"));
		Assert.assertTrue(cluster0.contains("UserB"));
		Assert.assertTrue(cluster0.contains("UserC"));
		Assert.assertEquals(2, cluster1.size());
		Assert.assertTrue(cluster1.contains("UserA"));
		Assert.assertTrue(cluster1.contains("UserD"));

	}

	@Test
	public void testSNA() {
		response = ss.analysisSocialNetwork("0", "0");
		String json = (String) response.getEntity();
		json = json.substring(1, json.length()-1);
		Gson gson = new Gson();
		String[] userArray = gson.fromJson(json, String[].class);
		List<String> userList = Arrays.asList(userArray);
		Assert.assertTrue(userList.contains("UserA"));
		Assert.assertTrue(userList.contains("UserB"));
		Assert.assertTrue(userList.contains("UserC"));
		Assert.assertTrue(userList.contains("UserD"));
	}

}

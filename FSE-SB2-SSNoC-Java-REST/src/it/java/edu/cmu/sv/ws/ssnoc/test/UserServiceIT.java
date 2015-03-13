package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertCreated;
import static com.eclipsesource.restfuse.Assert.assertNotFound;
import static com.eclipsesource.restfuse.Assert.assertOk;
import static com.eclipsesource.restfuse.Assert.assertUnauthorized;

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


/*
 * Before running tests, create a user "persistent"
 */


@RunWith(HttpJUnitRunner.class)
public class UserServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");

	@Context
	public Response response;


	/*
	 * Create a persistent user - used for other tests
	 */
	@HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"persistent\",\"password\":\"Kiran\"}", order = 1)
	public void testPersistentUserCreation() {
		Assert.assertTrue((response.getStatus() == 200) ||
				(response.getStatus() == 201) || (response.getStatus() == 400));
	}


	/*
	 * Test Get user info for all users - expects 200
	 */
	@HttpTest(method = Method.GET, path = "/users", order = 2)
	public void testUsersFound() {
		assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);
		Assert.assertEquals("Not a Json List", messg.charAt(0), '[');
		Assert.assertEquals("Not a Json List", messg.charAt(messg.length()-1), ']');
	}

	/*
	 * Test Get user info for existing user - expects 200
	 */
	@HttpTest(method = Method.GET, path = "/user/persistent", order = 3)
	public void testUserRecord() {
		assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);
		Assert.assertEquals("Not a User Object", messg.charAt(0), '{');
		Assert.assertEquals("Not a User Object", messg.charAt(messg.length()-1), '}');
	}

	/*
	 * Test Get user info for non existing user - expects 404
	 */
	@HttpTest(method = Method.GET, path = "/user/nonexistant", order = 4)
	public void testNoRecord() {
		assertNotFound(response);
	}

	/*
	 * Test user signup - Expects 201
	 */
	@HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"Surya\",\"password\":\"Kiran\"}", order = 5)
	public void testUserCreation() {
		if (response.getStatus() == 200) {
			testUserDeletion();
			testUserCreation();
		}
		assertCreated(response);
	}


	/*
	 * Test whether deletion works for the user Surya. - Expects 200
	 */
	@HttpTest(method = Method.DELETE, path = "/user/Surya", order = 6)
	public void testUserDeletion() {
		assertOk(response);
	}


	/*
	 * Test user authentication for an existing user with wrong password - expects 401
	 */

	@HttpTest(method = Method.POST, path = "/user/persistent/authenticate", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"persistant\", \"password\":\"Kiranasd\"}", order = 7)
	public void testInvalidLogin() {
		assertUnauthorized(response);
	}

	/*
	 * Test user authentication for a non-existing user - expects 404
	 */
	@HttpTest(method = Method.POST, path = "/user/nonexistant/authenticate", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"nonexistant\", \"password\":\"Kiran\"}", order = 8)
	public void testUnauthorisedLogin() {
		assertNotFound(response);
	}

	/*
	 * Test the update of a nonexistant User - expects 404
	 */
	@HttpTest(method = Method.PUT, path = "/user/nonexistant", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"nonexistant\"}", order = 9)
	public void testUpdateNotExisting() {
		assertUnauthorized(response);
	}


	/*
	 * Test the update of an existing user with no username change - expects 200
	 */
	@HttpTest(method = Method.PUT, path = "/user/persistent", type = MediaType.APPLICATION_JSON,
			content = "{\"password\":\"new-password123\"}", order = 10)
	public void testUpdateUserInfo() {
		assertOk(response);
	}

	/*
	 * Test the update of an existing user with username change - expects 201
	 */
	/*@HttpTest(method = Method.PUT, path = "/user/persistent", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"persistent2\"}", order = 11)
	public void testUpdateUserName() {
		assertCreated(response);
	}*/

	/*
	 * Test the update of an existing user with username change - expects 201
	 */
	/*@HttpTest(method = Method.PUT, path = "/user/persistent2", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"persistent\"}", order = 12)
	public void testUpdateUserNameAgain() {
		assertCreated(response);
	}*/

}

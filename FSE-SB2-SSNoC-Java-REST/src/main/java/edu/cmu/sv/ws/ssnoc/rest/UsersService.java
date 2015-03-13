package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/users")
public class UsersService extends BaseService {
	/**
	 * This method loads all active users in the system.
	 *
	 * @return - List of all active users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "users")
	public List<User> loadUsers() {
		Log.enter();

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();

			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/active")
	public List<User> loadActiveUsers() {
		Log.enter();
		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadActiveUsers();
			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		return users;
	}


	//get all the users who a user has chatted with
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{username}/chatbuddies")
	public List<User> loadChat(@PathParam("username") String username) {
		Log.enter(username);

		List<MessagePO> messages = new ArrayList<MessagePO>();
		List<User> chatusers = new ArrayList<User>();

		try {
			messages = DAOFactory.getInstance().getMessageDAO().findByAORT(username);
			for(MessagePO message : messages){
				int flag = 1;
				User chatuser = new User();
				IUserDAO dao = DAOFactory.getInstance().getUserDAO();


				if(username.equals(message.getAuthor())){
					//check if the username already exists in the User array
					for(int    i=0;    i<chatusers.size();    i++)    {
					       User    temp    =    chatusers.get(i);
					       if (temp.getUserName().equals(message.getTarget())){
					    	   flag = 0;
					    	   break;
					       }
					   }

					if(flag == 1){
						UserPO existingUser = dao.findByName(message.getTarget());
						if(existingUser != null){
							chatuser = ConverterUtils.convert(existingUser);
							chatusers.add(chatuser);
						}
					}
				}
				else{

					for(int    i=0;    i<chatusers.size();    i++)    {
					       User    temp    =    chatusers.get(i);
					       if (temp.getUserName().equals(message.getAuthor())){
					    	   flag = 0;
					    	   break;
					       }
					   }
					if(flag == 1){
						UserPO existingUser = dao.findByName(message.getAuthor());
						if(existingUser != null){
							chatuser = ConverterUtils.convert(existingUser);
							chatusers.add(chatuser);
						}
					}
				}

				if (chatusers.size() == 0)
					Log.debug("no chatted user  exists");

			}
			System.out.println(chatusers);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(chatusers);
		}

		return chatusers;
	}
}

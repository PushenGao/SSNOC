package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

@Path("/message")
public class MessageService extends BaseService {
	// get the message by id
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{messageId}")
	public Message loadMessage(@PathParam("messageId") long messageId) {
		Log.enter(messageId);

		Message message = null;
		try {
			MessagePO po = DAOFactory.getInstance().getMessageDAO()
					.findId(messageId);
			message = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(message);
		}

		return message;
	}

	// send a message to a user
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{sendingUsername}/{receivingUsername}")
	public Response sendMessageChat(
			@PathParam("sendingUsername") String sendingUsername,
			@PathParam("receivingUsername") String receivingUsername,
			Message message) {
		Log.enter(message);
		Message resp = new Message();
		IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
		UserPO existingSendingUser = userDao.findByName(sendingUsername);
		UserPO existingReceivingUser = userDao.findByName(receivingUsername);
		if ((existingSendingUser != null) && (existingReceivingUser != null)) {
			message.setAuthor(sendingUsername);
			message.setTarget(receivingUsername);
			message.setMessageType("CHAT");
			MessagePO po = ConverterUtils.convert(message);
			IMessageDAO mDao = DAOFactory.getInstance().getMessageDAO();
			mDao.save(po);
			resp = ConverterUtils.convert(po);
		} else {
			Log.warn("Username doesn't exist.");
			return notFound();
		}
		return created(resp);
	}


	// post a message on the wall, or to the announcement page

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response sendMessageWall(@PathParam("userName") String pathName,
			Message message) {
		Log.enter(message);
		Message resp = new Message();
		String userName;
		if (pathName.equals("announcement")) {
			userName = message.getAuthor();
			message.setMessageType("ANNOUNCEMENT");
			Log.warn(userName);
		} else {
			message.setAuthor(pathName);
			message.setMessageType("WALL");
			IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = userDao.findByName(pathName);
			if (existingUser == null)
				return notFound();
		}
		MessagePO po = ConverterUtils.convert(message);
		System.out.println(po);
		IMessageDAO mDao = DAOFactory.getInstance().getMessageDAO();
		mDao.save(po);
		resp = ConverterUtils.convert(po);
		return created(resp);
	}




}

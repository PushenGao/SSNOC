package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

@Path("/messages")
public class MessagesService extends BaseService {

	// get all the message between two users
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{author}/{target}")
	public List<Message> loadChat(@PathParam("author") String author,
			@PathParam("target") String target) {
		Log.enter(author);
		Log.enter(target);

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
		UserPO existingSendingUser = userDao.findByName(author);
		UserPO existingReceivingUser = userDao.findByName(target);
		if ((existingSendingUser != null) && (existingReceivingUser != null)) {
			try {
				po = DAOFactory.getInstance().getMessageDAO()
						.findByAT(author, target);
				for (MessagePO mpo : po) {
					Message mdto = ConverterUtils.convert(mpo);
					dto.add(mdto);
				}
				// message = ConverterUtils.convert(po);
			} catch (Exception e) {
				handleException(e);
			} finally {
				Log.exit(dto);
			}
		}
		return dto;
	}

	// get all the message between two users
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{author}/{target}/visible")
	public List<Message> loadChatVisible(@PathParam("author") String author,
			@PathParam("target") String target) {
		Log.enter(author);
		Log.enter(target);

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
		UserPO existingSendingUser = userDao.findByName(author);
		UserPO existingReceivingUser = userDao.findByName(target);
		String status = "active";
		if ((existingSendingUser != null) && (existingReceivingUser != null)
				&& (existingSendingUser.getStatus().equals(status))
				&& (existingReceivingUser.getStatus().equals(status))) {
			try {
				po = DAOFactory.getInstance().getMessageDAO()
						.findByAT(author, target);
				for (MessagePO mpo : po) {
					Message mdto = ConverterUtils.convert(mpo);
					dto.add(mdto);
				}
				// message = ConverterUtils.convert(po);
			} catch (Exception e) {
				handleException(e);
			} finally {
				Log.exit(dto);
			}
		}
		return dto;
	}

	// get all the message between two users
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/wall")
	public List<Message> loadWALL() {
		Log.enter("WALL");

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		try {
			po = DAOFactory.getInstance().getMessageDAO()
					.findMessageByType("WALL");
			for (MessagePO mpo : po) {
				Message mdto = ConverterUtils.convert(mpo);
				dto.add(mdto);
			}
			// message = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dto);
		}

		return dto;
	}

	// get all the message between two users
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/wall/visible")
	public List<Message> loadWALLVisible() {
		Log.enter("WALL Visible");

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		try {
			po = DAOFactory.getInstance().getMessageDAO()
					.findMessageByTypeVisible("WALL");
			for (MessagePO mpo : po) {
				Message mdto = ConverterUtils.convert(mpo);
				dto.add(mdto);
			}
			// message = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dto);
		}

		return dto;
	}

	// get all the announcements
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/announcement")
	public List<Message> loadAnnouncements() {
		Log.enter("GET ANNOUNCEMENTS");

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		try {
			po = DAOFactory.getInstance().getMessageDAO()
					.findMessageByType("ANNOUNCEMENT");
			for (MessagePO mpo : po) {
				Message mdto = ConverterUtils.convert(mpo);
				dto.add(mdto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dto);
		}
		return dto;
	}

	// get all the message between two users
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/announcement/visible")
	public List<Message> loadAnnouncementVisible() {
		Log.enter("announcement Visible");

		List<MessagePO> po = new ArrayList<MessagePO>();
		List<Message> dto = new ArrayList<Message>();
		try {
			po = DAOFactory.getInstance().getMessageDAO()
					.findMessageByTypeVisible("ANNOUNCEMENT");
			for (MessagePO mpo : po) {
				Message mdto = ConverterUtils.convert(mpo);
				dto.add(mdto);
			}
			// message = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(dto);
		}

		return dto;
	}

}

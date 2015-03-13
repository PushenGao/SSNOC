package edu.cmu.sv.ws.ssnoc.common.utils;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryCrumbPO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusCrumbPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.MemoryCrumb;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.StatusCrumb;
import edu.cmu.sv.ws.ssnoc.dto.User;

/**
 * This is a utility class used to convert PO (Persistent Objects) and View
 * Objects into DTO (Data Transfer Objects) objects, and vice versa. <br/>
 * Rather than having the conversion code in all classes in the rest package,
 * they are maintained here for code re-usability and modularity.
 *
 */
public class ConverterUtils {
	/**
	 * Convert UserPO to User DTO object.
	 *
	 * @param po
	 *            - User PO object
	 *
	 * @return - User DTO Object
	 */
	public static final User convert(UserPO po) {
		if (po == null) {
			return null;
		}

		User dto = new User();
		dto.setUserName(po.getUserName());
		dto.setCreatedAt(po.getCreatedAt());
		dto.setModifiedAt(po.getModifiedAt());
		dto.setLastStatusCode(po.getLastStatusCode());
		dto.setStatus(po.getStatus());
		dto.setPrivilege(po.getPrivilege());

		return dto;
	}

	/**
	 * Convert User DTO to UserPO object
	 *
	 * @param dto
	 *            - User DTO object
	 *
	 * @return - UserPO object
	 */
	public static final UserPO convert(User dto) {
		if (dto == null) {
			return null;
		}

		UserPO po = new UserPO();
		po.setUserName(dto.getUserName());
		po.setPassword(dto.getPassword());
		po.setCreatedAt(dto.getCreatedAt());
		po.setModifiedAt(dto.getModifiedAt());
		po.setLastStatusCode(dto.getLastStatusCode());
		po.setStatus(dto.getStatus());
		po.setPrivilege(dto.getPrivilege());

		return po;
	}

	public static final StatusCrumb convert(StatusCrumbPO po) {
		if (po == null) {
			return null;
		}

		StatusCrumb dto = new StatusCrumb();
		dto.setUpdatedAt(po.getCreatedAt());
		dto.setStatusCode(po.getStatusCode());
		dto.setUserName(po.getUserName());

		return dto;

	}

	public static final MemoryCrumbPO convert(MemoryCrumb crumb) {
		if (crumb == null) {
			return null;
		}

		MemoryCrumbPO mempo = new MemoryCrumbPO();
		mempo.setCreatedAt(crumb.getCreatedAt());
		mempo.setRemainingPersistent(crumb.getRemainingPersistent());
		mempo.setRemainingVolative(crumb.getRemainingVolatile());
		mempo.setUsedPersistent(crumb.getUsedPersistent());
		mempo.setUsedVolative(crumb.getUsedVolatile());

		return mempo;

	}

	public static final StatusCrumbPO convert(StatusCrumb dto) {
		if (dto == null) {
			return null;
		}

		StatusCrumbPO po = new StatusCrumbPO();
		po.setCreatedAt(dto.getUpdatedAt());
		po.setStatusCode(dto.getStatusCode());
		po.setUserName(dto.getUserName());

		return po;
	}
	/**
	 * Convert MessagePO to Message DTO object.
	 *
	 * @param po
	 *            - Message PO object
	 *
	 * @return - Message DTO Object
	 */
	public static final Message convert(MessagePO messagepo) {
		if (messagepo == null) {
			return null;
		}

		Message messagedto = new Message();
		messagedto.setContent(messagepo.getContent());
		messagedto.setAuthor(messagepo.getAuthor());
		messagedto.setMessageType(messagepo.getMessageType());
		messagedto.setPostedAt(messagepo.getPostedAt());
		messagedto.setTarget(messagepo.getTarget());

		return messagedto;
	}

	public static final MemoryCrumb convert(MemoryCrumbPO crumb) {
		if (crumb == null) {
			return null;
		}

		MemoryCrumb mem = new MemoryCrumb();
		mem.setCreatedAt(crumb.getCreatedAt());
		mem.setRemainingPersistent(crumb.getRemainingPersistent());
		mem.setRemainingVolatile(crumb.getRemainingVolative());
		mem.setUsedPersistent(crumb.getUsedPersistent());
		mem.setUsedVolatile(crumb.getUsedVolative());

		return mem;

	}
	/**
	 * Convert Message DTO to MessagePO object
	 *
	 * @param dto
	 *            - Message DTO object
	 *
	 * @return - MessagePO object
	 */
	public static final MessagePO convert(Message messagedto) {
		if (messagedto == null) {
			return null;
		}

		MessagePO messagepo = new MessagePO();
		messagepo.setContent(messagedto.getContent());
		messagepo.setAuthor(messagedto.getAuthor());
		messagepo.setMessageType(messagedto.getMessageType());
		messagepo.setPostedAt(messagedto.getPostedAt());
		messagepo.setTarget(messagedto.getTarget());

		return messagepo;
	}

	public static final MessagePO convert(String body){
		if(body == null)
			return null;
		MessagePO mpo = new MessagePO();
		String[] subStringSet;
		char c = body.charAt(body.length()-1);
		if(c == '}')
			subStringSet = body.substring(1, body.length()-1).split(",");
		else
			 subStringSet = body.substring(1, body.length()).split(",");
		for(String subString: subStringSet){
			String[] part = subString.split(":");
			if(part[0].substring(1, part[0].length()-1).equals("content"))
				mpo.setContent(part[1].substring(1, part[1].length()-1));
			else if(part[0].substring(1, part[0].length()-1).equals("author"))
				mpo.setAuthor(part[1].substring(1, part[1].length()-1));
			else if(part[0].substring(1, part[0].length()-1).equals("messageType"))
				mpo.setMessageType(part[1].substring(1, part[1].length()-1));
			else if(part[0].substring(1, part[0].length()-1).equals("postedAt"))
				mpo.setPostedAt(part[1].substring(1, part[1].length()-1));
			else if(part[0].substring(1, part[0].length()-1).equals("target"))
				mpo.setTarget(part[1].substring(1, part[1].length()-1));
		}
		return mpo;
	}

	public static final UserPO stringConvertToUserPO(String body){
		if(body == null)
			return null;
		UserPO upo = new UserPO();
		String[] subStringSet;
		char c = body.charAt(body.length()-1);
		if(c == '}')
			subStringSet = body.substring(1, body.length()-1).split(",");
		else
			 subStringSet = body.substring(1, body.length()).split(",");
		for(String subString: subStringSet){
			String[] part = subString.split(":");
			if(part[0].substring(1, part[0].length()-1).equals("lastStatusCode"))
				upo.setLastStatusCode(part[1].substring(1, part[1].length()-1));
			else if(part[0].substring(1, part[0].length()-1).equals("userName"))
				upo.setUserName(part[1].substring(1, part[1].length()-1));
		}
		return upo;
	}
}

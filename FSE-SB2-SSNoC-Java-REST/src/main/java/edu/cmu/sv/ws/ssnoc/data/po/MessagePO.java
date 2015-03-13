package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save all Messages in the system.
 * This contains information like the content, username, type, target, and message id.
 * Information is saved in SSN_MESSAGE table.
 *
 */
public class MessagePO {
	private long messageId;
	private String content;
	private String author; //username
	private String messageType;
	private String postedAt;
	private String target;

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}


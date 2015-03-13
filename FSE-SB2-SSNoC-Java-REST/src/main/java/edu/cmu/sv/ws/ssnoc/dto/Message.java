package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

/**
 * this object contains message information that is responded as part of the REST
 * API request.
 * 
 */
public class Message {
	private String content;
	private String author; //username
	private String messageType;
	private String postedAt;
	private String target;

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



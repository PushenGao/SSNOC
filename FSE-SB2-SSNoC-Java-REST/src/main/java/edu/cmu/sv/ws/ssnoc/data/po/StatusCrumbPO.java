package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

public class StatusCrumbPO {
	private long crumbID;
	private String userName;
	private String statusCode;
	private String createdAt;

	public long getCrumbID() {
		return crumbID;
	}

	public void setCrumbID(long crumbID) {
		this.crumbID = crumbID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

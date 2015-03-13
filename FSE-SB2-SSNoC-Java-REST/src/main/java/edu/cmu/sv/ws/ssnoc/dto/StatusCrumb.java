package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class StatusCrumb {
	private String userName;
	private String statusCode;
	private String updatedAt;

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

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

public class MemoryCrumbPO {
	private long createdAt;
	private long crumbID;
	private long usedVolative;
	private long remainingVolative;
	private long usedPersistent;
	private long remainingPersistent;

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getCrumbID() {
		return crumbID;
	}

	public void setCrumbID(long crumbID) {
		this.crumbID = crumbID;
	}

	public long getUsedVolative() {
		return usedVolative;
	}

	public void setUsedVolative(long usedVolative) {
		this.usedVolative = usedVolative;
	}

	public long getRemainingVolative() {
		return remainingVolative;
	}

	public void setRemainingVolative(long remainingVolative) {
		this.remainingVolative = remainingVolative;
	}

	public long getUsedPersistent() {
		return usedPersistent;
	}

	public void setUsedPersistent(long usedPersistent) {
		this.usedPersistent = usedPersistent;
	}

	public long getRemainingPersistent() {
		return remainingPersistent;
	}

	public void setRemainingPersistent(long remainingPersistent) {
		this.remainingPersistent = remainingPersistent;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}


}

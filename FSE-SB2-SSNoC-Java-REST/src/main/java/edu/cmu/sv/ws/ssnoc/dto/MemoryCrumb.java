package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class MemoryCrumb {
	private long CreatedAt;
	private long usedVolatile;
	private long remainingVolatile;
	private long usedPersistent;
	private long remainingPersistent;

	public long getUsedVolatile() {
		return usedVolatile;
	}

	public long getRemainingVolatile() {
		return remainingVolatile;
	}
	public long getUsedPersistent() {
		return usedPersistent;
	}
	public long getRemainingPersistent() {
		return remainingPersistent;
	}
	
	public long getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(long createdAt) {
		CreatedAt = createdAt;
	}

	public void setUsedVolatile(long usedVolatile) {
		this.usedVolatile = usedVolatile;
	}

	public void setRemainingVolatile(long remainingVolatile) {
		this.remainingVolatile = remainingVolatile;
	}

	public void setUsedPersistent(long usedPersistent) {
		this.usedPersistent = usedPersistent;
	}

	public void setRemainingPersistent(long remainingPersistent) {
		this.remainingPersistent = remainingPersistent;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}

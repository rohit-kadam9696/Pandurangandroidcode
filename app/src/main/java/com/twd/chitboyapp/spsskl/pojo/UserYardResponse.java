package com.twd.chitboyapp.spsskl.pojo;

public class UserYardResponse extends MainResponse {

	private String userId, userName, currentYardId;
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCurrentYardId() {
		return currentYardId;
	}

	public void setCurrentYardId(String currentYardId) {
		this.currentYardId = currentYardId;
	}

}

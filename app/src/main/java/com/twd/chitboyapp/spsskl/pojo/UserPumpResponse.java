package com.twd.chitboyapp.spsskl.pojo;

public class UserPumpResponse extends MainResponse {

	private String userId, userName, currentPumpId;
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

	public String getCurrentPumpId() {
		return currentPumpId;
	}

	public void setCurrentPumpId(String currentPumpId) {
		this.currentPumpId = currentPumpId;
	}

}

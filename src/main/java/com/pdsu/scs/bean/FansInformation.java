package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 */
public class FansInformation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserInformation user;
	
	private boolean islike;

	public UserInformation getUser() {
		return user;
	}

	public void setUser(UserInformation user) {
		this.user = user;
	}

	public boolean isIslike() {
		return islike;
	}

	public void setIslike(boolean islike) {
		this.islike = islike;
	}

	public FansInformation(UserInformation user, boolean islike) {
		super();
		this.user = user;
		this.islike = islike;
	}

	@Override
	public String toString() {
		return "FansInformation [user=" + user + ", islike=" + islike + "]";
	}
	
	public FansInformation() {
	}
	
}

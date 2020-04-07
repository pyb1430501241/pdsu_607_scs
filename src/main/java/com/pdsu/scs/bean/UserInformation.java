package com.pdsu.scs.bean;

public class UserInformation {
    private Integer id;

    private Integer uid;

    private String password;

    private String username;

    private String college;

    private String clazz;

    private String time;

    private Integer accountStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college == null ? null : college.trim();
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz == null ? null : clazz.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

	@Override
	public String toString() {
		return "UserInformation [id=" + id + ", uid=" + uid + ", password=" + password + ", username=" + username
				+ ", college=" + college + ", clazz=" + clazz + ", time=" + time + ", accountStatus=" + accountStatus
				+ "]";
	}

	public UserInformation(Integer id, Integer uid, String password, String username, String college, String clazz,
			String time, Integer accountStatus) {
		super();
		this.id = id;
		this.uid = uid;
		this.password = password;
		this.username = username;
		this.college = college;
		this.clazz = clazz;
		this.time = time;
		this.accountStatus = accountStatus;
	}
    
    public UserInformation() {
	}
}
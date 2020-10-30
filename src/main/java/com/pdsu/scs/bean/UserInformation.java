package com.pdsu.scs.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户信息
 * @author Admin
 *
 */
public class UserInformation implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotNull(message = "学号必须为纯数字")
    private Integer uid;

	@NotBlank(message = "密码必须为6~20个字母或数字特殊字符的组合")
	@Size(min = 6, max = 20, message = "密码必须为6~20个字母或数字特殊字符的组合")
    private String password;

	@NotBlank(message = "用户名为4~16英文字符, 数字, 或2~8个汉字")
	@Size(min = 2, max = 16, message = "用户名为4~16英文字符, 数字, 或2~8个汉字")
    private String username;

    private String college;

    private String clazz;

    private String time;

    private Integer accountStatus;
    
    private String imgpath;
    
    private String email;

    private Integer systemNotifications;

	public UserInformation createUserInformationByThis() {
		try {
			return (UserInformation) clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "UserInformation{" +
				"id=" + id +
				", uid=" + uid +
				", password='" + password + '\'' +
				", username='" + username + '\'' +
				", college='" + college + '\'' +
				", clazz='" + clazz + '\'' +
				", time='" + time + '\'' +
				", accountStatus=" + accountStatus +
				", imgpath='" + imgpath + '\'' +
				", email='" + email + '\'' +
				", systemNotifications=" + systemNotifications +
				'}';
	}

	public UserInformation(Integer uid, String password, String username, String college, String clazz, String time, Integer accountStatus, String imgpath, String email, Integer systemNotifications) {
		this.uid = uid;
		this.password = password;
		this.username = username;
		this.college = college;
		this.clazz = clazz;
		this.time = time;
		this.accountStatus = accountStatus;
		this.imgpath = imgpath;
		this.email = email;
		this.systemNotifications = systemNotifications;
	}

	public void setSystemNotifications(Integer systemNotifications) {
		this.systemNotifications = systemNotifications;
	}

	public Integer getSystemNotifications() {
		return systemNotifications;
	}

	public UserInformation(Integer id, Integer uid, String password, String username, String college, String clazz,
						   String time, Integer accountStatus, String imgpath, String email) {
		super();
		this.id = id;
		this.uid = uid;
		this.password = password;
		this.username = username;
		this.college = college;
		this.clazz = clazz;
		this.time = time;
		this.accountStatus = accountStatus;
		this.imgpath = imgpath;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserInformation(Integer id, Integer uid, String password, String username, String college, String clazz,
			String time, Integer accountStatus, String imgpath) {
		super();
		this.id = id;
		this.uid = uid;
		this.password = password;
		this.username = username;
		this.college = college;
		this.clazz = clazz;
		this.time = time;
		this.accountStatus = accountStatus;
		this.imgpath = imgpath;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

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
    
    public UserInformation(Integer uid) {
    	this(null, uid, null, null, null, null , null, null);
    }
}
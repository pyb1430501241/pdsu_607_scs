package com.pdsu.scs.bean;
/**
 * @author 半梦
 */
public class FileInformation {

	private UserInformation user;
	
	private WebFile webfile;
	
	private Integer downloads;

	public UserInformation getUser() {
		return user;
	}

	public void setUser(UserInformation user) {
		this.user = user;
	}

	public WebFile getWebfile() {
		return webfile;
	}

	public void setWebfile(WebFile webfile) {
		this.webfile = webfile;
	}

	public Integer getDownloads() {
		return downloads;
	}

	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}

	public FileInformation(UserInformation user, WebFile webfile, Integer downloads) {
		super();
		this.user = user;
		this.webfile = webfile;
		this.downloads = downloads;
	}

	public FileInformation() {
	}

	@Override
	public String toString() {
		return "FileInformation [user=" + user + ", webfile=" + webfile + ", downloads=" + downloads + "]";
	}
	
}

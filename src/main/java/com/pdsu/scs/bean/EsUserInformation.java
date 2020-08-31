package com.pdsu.scs.bean;

/**
 * 搜索引擎所保存的数据
 * @author 半梦
 *
 */
public class EsUserInformation {
	
	private Integer uid;
	
	private String username;
	
	private String imgpath;
	
	private Integer blobnum;
	
	private Integer likenum;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public Integer getBlobnum() {
		return blobnum;
	}

	public void setBlobnum(Integer blobnum) {
		this.blobnum = blobnum;
	}

	public Integer getLikenum() {
		return likenum;
	}

	public void setLikenum(Integer likenum) {
		this.likenum = likenum;
	}

	@Override
	public String toString() {
		return "{\r\n" + 
				"  \"uid\" : " + uid + ",\r\n" + 
				"  \"username\" : \"" + username + "\",\r\n" + 
				"  \"imgpath\" : \"" + imgpath + "\",\r\n" + 
				"  \"blobnum\" : " + blobnum + ",\r\n" + 
				"  \"likenum\" : " + likenum + "\r\n" + 
				"}";
	}

	public EsUserInformation(Integer uid, Integer blobnum, String imgpath, Integer likenum, String username) {
		super();
		this.uid = uid;
		this.username = username;
		this.imgpath = imgpath;
		this.blobnum = blobnum;
		this.likenum = likenum;
	}
	
	public EsUserInformation() {
	}
	
}

package com.pdsu.scs.bean;

/**
 * 博客信息
 * @author 半梦
 *
 */
public class BlobInformation {
	
	private UserInformation user;
	
	private WebInformation web;
	
	private Integer visit;
	
	private Integer thumbs;
	
	private Integer collection;

	public WebInformation getWeb() {
		return web;
	}

	public void setWeb(WebInformation web) {
		this.web = web;
	}

	public Integer getVisit() {
		return visit;
	}

	public void setVisit(Integer visit) {
		this.visit = visit;
	}

	public Integer getThumbs() {
		return thumbs;
	}

	public void setThumbs(Integer thumbs) {
		this.thumbs = thumbs;
	}

	public Integer getCollection() {
		return collection;
	}

	public void setCollection(Integer collection) {
		this.collection = collection;
	}

	public BlobInformation(UserInformation user, WebInformation web, Integer visit, Integer thumbs,
			Integer collection) {
		super();
		this.user = user;
		this.web = web;
		this.visit = visit;
		this.thumbs = thumbs;
		this.collection = collection;
	}

	public UserInformation getUser() {
		return user;
	}

	public void setUser(UserInformation user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "BlobInformation [user=" + user + ", web=" + web + ", visit=" + visit + ", thumbs=" + thumbs
				+ ", collection=" + collection + "]";
	}

	public BlobInformation(WebInformation web, Integer visit, Integer thumbs, Integer collection) {
		super();
		this.web = web;
		this.visit = visit;
		this.thumbs = thumbs;
		this.collection = collection;
	}
	
	public BlobInformation() {
	}
	
}

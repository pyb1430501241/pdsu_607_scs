package com.pdsu.scs.bean;

/**
 * es 存储的 blob 信息
 * @author 半梦
 *
 */
public class EsBlobInformation {
	
	private Integer webid;
	
	private String title;
	
	private String description;

	public Integer getWebid() {
		return webid;
	}

	public void setWebid(Integer webid) {
		this.webid = webid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "EsBlobInformation [webid=" + webid + ", title=" + title + ", description=" + description + "]";
	}

	public EsBlobInformation(Integer webid, String description, String title) {
		super();
		this.webid = webid;
		this.title = title;
		this.description = description;
	}
	
	public EsBlobInformation() {
	}
	
}

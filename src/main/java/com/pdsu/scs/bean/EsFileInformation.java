package com.pdsu.scs.bean;

/**
 * es 存储 File 信息
 * @author 半梦
 *
 */
public class EsFileInformation {

	private Integer fileid;
	
	private String title;
	
	private String description;

	public Integer getFileid() {
		return fileid;
	}

	public void setFileid(Integer fileid) {
		this.fileid = fileid;
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
	
	public EsFileInformation() {
	}

	public EsFileInformation(String description, String title, Integer fileid) {
		super();
		this.fileid = fileid;
		this.title = title;
		this.description = description;
	}

	@Override
	public String toString() {
		return "EsFileInformation [fileid=" + fileid + ", title=" + title + ", description=" + description + "]";
	}
	
}

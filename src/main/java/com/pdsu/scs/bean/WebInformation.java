package com.pdsu.scs.bean;

import java.util.Arrays;

public class WebInformation {
    private Integer id;

    private String title;

    private Integer uid;

    private Integer contype;

    private String subTime;

    private byte[] webData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getContype() {
        return contype;
    }

    public void setContype(Integer contype) {
        this.contype = contype;
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime == null ? null : subTime.trim();
    }

    public byte[] getWebData() {
        return webData;
    }

    public void setWebData(byte[] webData) {
        this.webData = webData;
    }

	public WebInformation(Integer id, String title, Integer uid, Integer contype, String subTime, byte[] webData) {
		super();
		this.id = id;
		this.title = title;
		this.uid = uid;
		this.contype = contype;
		this.subTime = subTime;
		this.webData = webData;
	}

	@Override
	public String toString() {
		return "WebInformation [id=" + id + ", title=" + title + ", uid=" + uid + ", contype=" + contype + ", subTime="
				+ subTime + ", webData=" + Arrays.toString(webData) + "]";
	}
    
    public WebInformation() {
	}
}
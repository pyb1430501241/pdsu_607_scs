package com.pdsu.scs.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 博客信息
 * @author Admin
 *
 */
public class WebInformation implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
    private String title;

    private Integer uid;

    private Integer contype;

    private String subTime;

    private byte[] webData;
    
    private String webDataString;
    
    public WebInformation(Integer id, String title, Integer uid, Integer contype, String subTime, byte[] webData,
			String webDataString) {
		super();
		this.id = id;
		this.title = title;
		this.uid = uid;
		this.contype = contype;
		this.subTime = subTime;
		this.webData = webData;
		this.webDataString = webDataString;
	}

	public String getWebDataString() {
		return webDataString;
	}

	public void setWebDataString(String webDataString) {
		this.webDataString = webDataString;
	}

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
				+ subTime + ", webData=" + Arrays.toString(webData) + ", webDataString=" + webDataString + "]";
	}
    
    public WebInformation() {
	}
}
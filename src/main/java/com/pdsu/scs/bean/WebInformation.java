package com.pdsu.scs.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

	@NotBlank(message = "文章标题必须为 30 个字符以内")
    @Size(min = 1, max = 30, message = "文章标题必须为 30 个字符以内")
    private String title;

    private Integer uid;

    @NotNull(message = "文章类型不可为空")
    private Integer contype;

    private String subTime;

    private byte[] webData;

    @NotBlank(message = "文章主题内容不可为空")
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


    public static WebInformation getWebInformation(WebInformation web) {
        WebInformation webInformation = new WebInformation();
        webInformation.setUid(web.getUid());
        webInformation.setTitle(web.getTitle());
        webInformation.setId(web.getId());
        webInformation.setSubTime(web.getSubTime());
        webInformation.setWebDataString(web.getWebDataString());
        webInformation.setContype(web.getContype());
        return webInformation;
	}
}
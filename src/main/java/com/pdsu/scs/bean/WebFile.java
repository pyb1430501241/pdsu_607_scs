package com.pdsu.scs.bean;

import sun.net.www.content.text.plain;

/**
 * 文件相关
 * @author 半梦
 *
 */
public class WebFile {
    private Integer id;

    private Integer uid;

    private String filePath;

    private String creattime;

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getCreattime() {
        return creattime;
    }

    public void setCreattime(String creattime) {
        this.creattime = creattime == null ? null : creattime.trim();
    }

	@Override
	public String toString() {
		return "WebFile [id=" + id + ", uid=" + uid + ", filePath=" + filePath + ", creattime=" + creattime + "]";
	}

	public WebFile(Integer id, Integer uid, String filePath, String creattime) {
		super();
		this.id = id;
		this.uid = uid;
		this.filePath = filePath;
		this.creattime = creattime;
	}
    
    public WebFile() {
	}
    
    public WebFile(Integer uid, String filePath, String creattime) {
    	this(null, uid, filePath, creattime);
    }
}
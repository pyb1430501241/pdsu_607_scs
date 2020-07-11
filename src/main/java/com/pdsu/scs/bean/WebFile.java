package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 文件相关
 * @author 半梦
 *
 */
public class WebFile implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer uid;

    private String title;

    private String description;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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
		return "WebFile [id=" + id + ", uid=" + uid + ", title=" + title + ", description=" + description
				+ ", filePath=" + filePath + ", creattime=" + creattime + "]";
	}

	public WebFile(Integer id, Integer uid, String title, String description, String filePath, String creattime) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.description = description;
		this.filePath = filePath;
		this.creattime = creattime;
	}
    
    public WebFile() {
	}

	public WebFile(Integer uid, String title, String description, String filePath, String creattime) {
		super();
		this.uid = uid;
		this.title = title;
		this.description = description;
		this.filePath = filePath;
		this.creattime = creattime;
	}
    

}
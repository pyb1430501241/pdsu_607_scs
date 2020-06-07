package com.pdsu.scs.bean;

/**
 * 文件处理类
 * @author 半梦
 *
 */
public class WebFile {
    private Integer id;

    private Integer uid;

    private String title;

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
		return "WebFile [id=" + id + ", uid=" + uid + ", title=" + title + ", filePath=" + filePath + ", creattime="
				+ creattime + "]";
	}

	public WebFile(Integer id, Integer uid, String title, String filePath, String creattime) {
		super();
		this.id = id;
		this.uid = uid;
		this.title = title;
		this.filePath = filePath;
		this.creattime = creattime;
	}
	
	public WebFile(Integer uid, String title, String filePath, String creattime) {
		super();
		this.uid = uid;
		this.title = title;
		this.filePath = filePath;
		this.creattime = creattime;
	}
    
    public WebFile() {
	}
}
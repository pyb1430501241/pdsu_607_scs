package com.pdsu.scs.bean;

/**
 * 文件下载
 * @author Admin
 *
 */
public class WebFile {
    private Integer id;

    private Integer uid;

    private String filePath;

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

	@Override
	public String toString() {
		return "WebFile [id=" + id + ", uid=" + uid + ", filePath=" + filePath + "]";
	}

	public WebFile(Integer id, Integer uid, String filePath) {
		super();
		this.id = id;
		this.uid = uid;
		this.filePath = filePath;
	}
    
    public WebFile() {
	}
}
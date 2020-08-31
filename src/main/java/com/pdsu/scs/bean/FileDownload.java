package com.pdsu.scs.bean;

/**
 * 文件下载数据
 * @author 半梦
 *
 */
public class FileDownload {
    private Integer id;

    private Integer fileid;

    private Integer bid;

    private Integer uid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileid() {
        return fileid;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

	@Override
	public String toString() {
		return "FileDownload [id=" + id + ", fileid=" + fileid + ", bid=" + bid + ", uid=" + uid + "]";
	}

	public FileDownload(Integer fileid, Integer bid, Integer uid) {
		super();
		this.fileid = fileid;
		this.bid = bid;
		this.uid = uid;
	}
    
	public FileDownload() {
	}
}
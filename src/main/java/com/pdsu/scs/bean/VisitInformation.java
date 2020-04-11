package com.pdsu.scs.bean;

/**
 * 访问相关
 * @author Admin
 *
 */
public class VisitInformation {
    private Integer id;

    private Integer vid;

    private Integer sid;

    private Integer wid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getWid() {
        return wid;
    }

    public void setWid(Integer wid) {
        this.wid = wid;
    }

	public VisitInformation(Integer id, Integer vid, Integer sid, Integer wid) {
		super();
		this.id = id;
		this.vid = vid;
		this.sid = sid;
		this.wid = wid;
	}

	@Override
	public String toString() {
		return "VisitInformation [id=" + id + ", vid=" + vid + ", sid=" + sid + ", wid=" + wid + "]";
	}
    
    public VisitInformation() {
	}
}
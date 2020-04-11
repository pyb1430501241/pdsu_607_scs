package com.pdsu.scs.bean;

/**
 * 收藏
 * @author Admin
 *
 */
public class MyCollection {
    private Integer id;

    private Integer uid;

    private Integer wid;

    private Integer bid;

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

    public Integer getWid() {
        return wid;
    }

    public void setWid(Integer wid) {
        this.wid = wid;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }
    
    public MyCollection() {
	}

	public MyCollection(Integer id, Integer uid, Integer wid, Integer bid) {
		super();
		this.id = id;
		this.uid = uid;
		this.wid = wid;
		this.bid = bid;
	}

	@Override
	public String toString() {
		return "MyCollection [id=" + id + ", uid=" + uid + ", wid=" + wid + ", bid=" + bid + "]";
	}
    
    
}
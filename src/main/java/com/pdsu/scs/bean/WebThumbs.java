package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 点赞相关
 * @author 半梦
 *
 */
public class WebThumbs implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer uid;

    private Integer bid;

    private Integer webid;

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

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getWebid() {
        return webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }

	@Override
	public String toString() {
		return "WebThumbs [id=" + id + ", uid=" + uid + ", bid=" + bid + ", webid=" + webid + "]";
	}

	public WebThumbs(Integer id, Integer uid, Integer bid, Integer webid) {
		super();
		this.id = id;
		this.uid = uid;
		this.bid = bid;
		this.webid = webid;
	}

	public WebThumbs(Integer uid, Integer bid, Integer webid) {
		super();
		this.uid = uid;
		this.bid = bid;
		this.webid = webid;
	}
    
    public WebThumbs() {
	}
}
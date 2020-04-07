package com.pdsu.scs.bean;

public class RealName {
    private Integer id;

    private Integer uid;

    private String realName;

    private String idcard;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

	@Override
	public String toString() {
		return "RealName [id=" + id + ", uid=" + uid + ", realName=" + realName + ", idcard=" + idcard + "]";
	}

	public RealName(Integer id, Integer uid, String realName, String idcard) {
		super();
		this.id = id;
		this.uid = uid;
		this.realName = realName;
		this.idcard = idcard;
	}
    
    public RealName() {
	}
}
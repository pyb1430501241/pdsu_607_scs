package com.pdsu.scs.bean;

/**
 * 关注
 * @author Admin
 *
 */
public class MyLike {
    private Integer id;

    private Integer uid;

    private Integer likeId;

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

    public Integer getLikeId() {
        return likeId;
    }

    public void setLikeId(Integer likeId) {
        this.likeId = likeId;
    }

	@Override
	public String toString() {
		return "MyLike [id=" + id + ", uid=" + uid + ", likeId=" + likeId + "]";
	}

	public MyLike(Integer id, Integer uid, Integer likeId) {
		super();
		this.id = id;
		this.uid = uid;
		this.likeId = likeId;
	}
    
    public MyLike() {
	}
}
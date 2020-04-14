package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 头像
 * @author Admin
 *
 */
public class MyImage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer uid;

    private String imagePath;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

	@Override
	public String toString() {
		return "MyImage [id=" + id + ", uid=" + uid + ", imagePath=" + imagePath + "]";
	}

	public MyImage(Integer id, Integer uid, String imagePath) {
		super();
		this.id = id;
		this.uid = uid;
		this.imagePath = imagePath;
	}
    
    public MyImage() {
	}
    
    public MyImage(Integer uid, String imagePath){
    	this(null,uid,imagePath);
    }
}
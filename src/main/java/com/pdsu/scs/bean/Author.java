package com.pdsu.scs.bean;

/**
 * 作者信息
 * @author 半梦
 *
 */
public class Author {
	
	/**
	 * 学号
	 */
	private Integer uid;
	
	/**
	 * 昵称
	 */
	private String username;
	
	/**
	 * 头像地址
	 */
	private String imgpath;
	
	/**
	 * 原创数
	 */
	private Integer original;
	
	/**
	 * 粉丝数
	 */
	private Integer fans;
	
	/**
	 * 点赞数
	 */
	private Integer thumbs;
	
	/**
	 * 评论数
	 */
	private Integer comment;
	
	/**
	 * 访问量
	 */
	private Integer visits;
	
	/**
	 * 收藏量
	 */
	private Integer collection;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public Integer getThumbs() {
		return thumbs;
	}

	public void setThumbs(Integer thumbs) {
		this.thumbs = thumbs;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public Integer getVisits() {
		return visits;
	}

	public void setVisits(Integer visits) {
		this.visits = visits;
	}

	public Integer getCollection() {
		return collection;
	}

	public void setCollection(Integer collection) {
		this.collection = collection;
	}

	public Author(Integer uid, String username, String imgpath, Integer original, Integer fans, Integer thumbs,
			Integer comment, Integer visits, Integer collection) {
		super();
		this.uid = uid;
		this.username = username;
		this.imgpath = imgpath;
		this.original = original;
		this.fans = fans;
		this.thumbs = thumbs;
		this.comment = comment;
		this.visits = visits;
		this.collection = collection;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	@Override
	public String toString() {
		return "Author [uid=" + uid + ", username=" + username + ", imgpath=" + imgpath + ", original=" + original
				+ ", fans=" + fans + ", thumbs=" + thumbs + ", comment=" + comment + ", visits=" + visits
				+ ", collection=" + collection + "]";
	}

	public Author(Integer uid, String username, Integer original, Integer fans, Integer thumbs, Integer comment,
			Integer visits, Integer collection) {
		super();
		this.uid = uid;
		this.username = username;
		this.original = original;
		this.fans = fans;
		this.thumbs = thumbs;
		this.comment = comment;
		this.visits = visits;
		this.collection = collection;
	}
	
	public Author() {
	}
	
}

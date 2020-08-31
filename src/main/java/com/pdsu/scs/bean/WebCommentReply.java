package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 回复评论
 * @author 半梦
 *
 */
public class WebCommentReply implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    /**
         * 文章ID
     */
    private Integer wid;

    /**
         *  评论ID
     */
    private Integer cid;

    /**
         * 评论人UID
     */
    private Integer uid;
    
    /**
         * 被评论人UID
     */
    private Integer bid;
    
    private String username;
    
    private String imgpath;

    private String content;

    private Integer thumb;

    private String createtime;

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWid() {
        return wid;
    }

    public void setWid(Integer wid) {
        this.wid = wid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getThumb() {
        return thumb;
    }

    public void setThumb(Integer thumb) {
        this.thumb = thumb;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

	@Override
	public String toString() {
		return "WebCommentReply [id=" + id + ", wid=" + wid + ", cid=" + cid + ", uid=" + uid + ", bid=" + bid
				+ ", content=" + content + ", thumb=" + thumb + ", createtime=" + createtime + "]";
	}

	public WebCommentReply(Integer id, Integer wid, Integer cid, Integer uid, Integer bid, String content,
			Integer thumb, String createtime) {
		super();
		this.id = id;
		this.wid = wid;
		this.cid = cid;
		this.uid = uid;
		this.bid = bid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
	}

	public WebCommentReply(Integer cid, Integer uid, Integer bid, String content, Integer thumb, String createtime) {
		super();
		this.cid = cid;
		this.uid = uid;
		this.bid = bid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
	}
	
	public WebCommentReply(Integer wid, Integer cid, Integer uid, Integer bid, String content, Integer thumb,
			String createtime) {
		super();
		this.wid = wid;
		this.cid = cid;
		this.uid = uid;
		this.bid = bid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
	}

	public WebCommentReply() {
	}
}
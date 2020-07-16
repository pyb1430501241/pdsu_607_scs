package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 回复评论相关
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
     * 评论id
     */
    private Integer cid;

    /**
     * 评论人uid
     */
    private Integer uid;

    /**
     * 被评论人id
     */
    private Integer bid;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumb;

    /**
     * 回复时间
     */
    private String createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
		return "WebCommentReply [id=" + id + ", cid=" + cid + ", uid=" + uid + ", bid=" + bid + ", content=" + content
				+ ", thumb=" + thumb + ", createtime=" + createtime + "]";
	}

	public WebCommentReply(Integer id, Integer cid, Integer uid, Integer bid, String content, Integer thumb,
			String createtime) {
		super();
		this.id = id;
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
    
    public WebCommentReply() {
	}
    
}
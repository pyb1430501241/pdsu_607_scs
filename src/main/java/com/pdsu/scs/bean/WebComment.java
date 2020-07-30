package com.pdsu.scs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 评论相关
 * @author 半梦
 *
 */
public class WebComment implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
    
    /**
     * 网页id
     */
    private Integer wid;

    /**
     * 评论人uid
     */
    private Integer uid;
    
    private String username;
    
    private String imgpath;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer thumb;

    /**
     * 评论时间
     */
    private String createtime;

    /**
     * 状态
     */
    private Integer state;
    
    private List<WebCommentReply> commentReplyList;
    
    public WebComment(Integer id, Integer wid, Integer uid, String content, Integer thumb, String createtime,
			Integer state, List<WebCommentReply> commentReplyList) {
		super();
		this.id = id;
		this.wid = wid;
		this.uid = uid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
		this.state = state;
		this.commentReplyList = commentReplyList;
	}

	public List<WebCommentReply> getCommentReplyList() {
		return commentReplyList;
	}

	public void setCommentReplyList(List<WebCommentReply> commentReplyList) {
		this.commentReplyList = commentReplyList;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

	@Override
	public String toString() {
		return "WebComment [id=" + id + ", wid=" + wid + ", uid=" + uid + ", username=" + username + ", imgpath="
				+ imgpath + ", content=" + content + ", thumb=" + thumb + ", createtime=" + createtime + ", state="
				+ state + ", commentReplyList=" + commentReplyList + "]";
	}

	public WebComment(Integer id, Integer wid, Integer uid, String content, Integer thumb, String createtime,
			Integer state) {
		super();
		this.id = id;
		this.wid = wid;
		this.uid = uid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
		this.state = state;
	}

	public WebComment(Integer wid, Integer uid, String content, Integer thumb, String createtime, Integer state) {
		super();
		this.wid = wid;
		this.uid = uid;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
		this.state = state;
	}
    
    public WebComment() {
	}

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

	public WebComment(Integer wid, Integer uid, String username, String imgpath, String content, Integer thumb,
			String createtime, Integer state, List<WebCommentReply> commentReplyList) {
		super();
		this.wid = wid;
		this.uid = uid;
		this.username = username;
		this.imgpath = imgpath;
		this.content = content;
		this.thumb = thumb;
		this.createtime = createtime;
		this.state = state;
		this.commentReplyList = commentReplyList;
	}
    
    
}
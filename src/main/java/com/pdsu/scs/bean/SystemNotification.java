package com.pdsu.scs.bean;

/**
 * 系统通知
 * @author 半梦
 */
public class SystemNotification {
    private Integer id;

    private Integer uid;

    private String content;

    private Integer sid;

    private Integer unread;

    private String createtime;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    @Override
    public String toString() {
        return "SystemNotification{" +
                "id=" + id +
                ", uid=" + uid +
                ", content='" + content + '\'' +
                ", sid=" + sid +
                ", unread=" + unread +
                ", createtime='" + createtime + '\'' +
                '}';
    }

    public SystemNotification(Integer uid, String content, Integer sid, Integer unread, String createtime) {
        this.uid = uid;
        this.content = content;
        this.sid = sid;
        this.unread = unread;
        this.createtime = createtime;
    }

    public SystemNotification() {

    }
}
package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 * @create 2020-08-12 23:04
 */
public class BrowsingRecordInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer uid;

    private String username;

    private String imgpath;

    private String title;

    private Integer wfid;

    private Integer type;

    private String createtime;

    public BrowsingRecordInformation(Integer uid, String username, String imgpath, Integer wfid, Integer type, String createtime) {
        this.uid = uid;
        this.username = username;
        this.imgpath = imgpath;
        this.wfid = wfid;
        this.type = type;
        this.createtime = createtime;
    }

    public  BrowsingRecordInformation () {

    }

    public BrowsingRecordInformation(Integer uid, String username, String imgpath, String title, Integer wfid, Integer type, String createtime) {
        this.uid = uid;
        this.username = username;
        this.imgpath = imgpath;
        this.title = title;
        this.wfid = wfid;
        this.type = type;
        this.createtime = createtime;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWfid(Integer wfid) {
        this.wfid = wfid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getImgpath() {
        return imgpath;
    }

    public String getTitle() {
        return title;
    }

    public Integer getWfid() {
        return wfid;
    }

    public Integer getType() {
        return type;
    }

    public String getCreatetime() {
        return createtime;
    }

    @Override
    public String toString() {
        return "BrowsingRecordInformation{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", imgpath='" + imgpath + '\'' +
                ", title='" + title + '\'' +
                ", wfid=" + wfid +
                ", type=" + type +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}

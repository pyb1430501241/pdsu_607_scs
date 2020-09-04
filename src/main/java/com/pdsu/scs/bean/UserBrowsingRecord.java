package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 */
public class UserBrowsingRecord implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer wfid;

    private Integer tpye;

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

    public Integer getWfid() {
        return wfid;
    }

    public void setWfid(Integer wfid) {
        this.wfid = wfid;
    }

    public Integer getTpye() {
        return tpye;
    }

    public void setTpye(Integer tpye) {
        this.tpye = tpye;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public UserBrowsingRecord(Integer uid, Integer wfid, Integer tpye, String createtime) {
        this.uid = uid;
        this.wfid = wfid;
        this.tpye = tpye;
        this.createtime = createtime;
    }

    public UserBrowsingRecord() {

    }

    @Override
    public String toString() {
        return "UserBrowsingRecord{" +
                "id=" + id +
                ", uid=" + uid +
                ", wfid=" + wfid +
                ", tpye=" + tpye +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}
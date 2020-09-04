package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 */
public class UserClazzInformation implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer cid;

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

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public UserClazzInformation(Integer uid, Integer cid) {
        this.uid = uid;
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "UserClazzInformation{" +
                "id=" + id +
                ", uid=" + uid +
                ", cid=" + cid +
                '}';
    }

    public UserClazzInformation() {
    }
}
package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * @author 半梦
 */
public class UserRole implements Serializable {
    private Integer id;

    private Integer uid;

    private Integer roleid;

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

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public UserRole() {
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", uid=" + uid +
                ", roleid=" + roleid +
                '}';
    }

    public UserRole(Integer uid, Integer roleid) {
        this.uid = uid;
        this.roleid = roleid;
    }
}
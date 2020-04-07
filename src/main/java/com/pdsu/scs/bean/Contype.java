package com.pdsu.scs.bean;

public class Contype {
    private Integer id;

    private String contype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContype() {
        return contype;
    }

    public void setContype(String contype) {
        this.contype = contype == null ? null : contype.trim();
    }
}
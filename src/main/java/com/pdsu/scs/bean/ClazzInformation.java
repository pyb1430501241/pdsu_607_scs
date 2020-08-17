package com.pdsu.scs.bean;

/**
 * @author 半梦
 */
public class ClazzInformation {
    private Integer id;

    private String clazzname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClazzname() {
        return clazzname;
    }

    public void setClazzname(String clazzname) {
        this.clazzname = clazzname == null ? null : clazzname.trim();
    }

    public ClazzInformation(String clazzname) {
        this.clazzname = clazzname;
    }

    public ClazzInformation() {
    }

    @Override
    public String toString() {
        return "ClazzInformation{" +
                "id=" + id +
                ", clazzname='" + clazzname + '\'' +
                '}';
    }
}
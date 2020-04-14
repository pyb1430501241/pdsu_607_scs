package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 文章热度
 * @author Admin
 *
 */
public class WebHeat implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer webid;

    private String heat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWebid() {
        return webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat == null ? null : heat.trim();
    }
}
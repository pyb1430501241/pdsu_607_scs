package com.pdsu.scs.bean;

/**
 * 文章类型
 * @author Admin
 *
 */
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

	@Override
	public String toString() {
		return "Contype [id=" + id + ", contype=" + contype + "]";
	}

	public Contype(Integer id, String contype) {
		super();
		this.id = id;
		this.contype = contype;
	}
    
    public Contype() {
	}
}
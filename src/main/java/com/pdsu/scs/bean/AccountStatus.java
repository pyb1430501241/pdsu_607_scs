package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 账号状态
 * @author Admin
 *
 */
public class AccountStatus implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	@Override
	public String toString() {
		return "AccountStatus [id=" + id + ", status=" + status + "]";
	}

	public AccountStatus(Integer id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
    
    public AccountStatus() {
	}
}
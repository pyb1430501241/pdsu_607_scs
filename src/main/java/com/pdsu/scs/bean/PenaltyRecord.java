package com.pdsu.scs.bean;

import java.io.Serializable;

/**
 * 处罚信息
 * @author Admin
 *
 */
public class PenaltyRecord implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer uid;

    private String reasons;

    private String time;

    private String dutation;

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

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons == null ? null : reasons.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getDutation() {
        return dutation;
    }

    public void setDutation(String dutation) {
        this.dutation = dutation == null ? null : dutation.trim();
    }

	@Override
	public String toString() {
		return "PenaltyRecord [id=" + id + ", uid=" + uid + ", reasons=" + reasons + ", time=" + time + ", dutation="
				+ dutation + "]";
	}

	public PenaltyRecord(Integer id, Integer uid, String reasons, String time, String dutation) {
		super();
		this.id = id;
		this.uid = uid;
		this.reasons = reasons;
		this.time = time;
		this.dutation = dutation;
	}
    
    public PenaltyRecord() {
	}
}
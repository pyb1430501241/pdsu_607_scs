package com.pdsu.scs.bean;

/**
 * 文章标签
 * @author 半梦
 *
 */
public class WebLabel {
    private Integer id;

    private String label;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

	public WebLabel(String label) {
		super();
		this.label = label;
	}

	@Override
	public String toString() {
		return "WebLabel [id=" + id + ", label=" + label + "]";
	}
    
    
}
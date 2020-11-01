package com.pdsu.scs.exception.web.es;

/**
 * es 插入数据错误
 * @author Admin
 *
 */
public class InsertException extends EsException{

	private Integer webId;

	public InsertException(String exception) {
		super(exception);
	}

	public InsertException(String exception, Integer webId) {
		super(exception);
		this.webId = webId;
	}

	public Integer getWebId() {
		return webId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

package com.pdsu.scs.exception.web.es;

import com.pdsu.scs.exception.web.WebException;

/**
 * es 相关异常
 * @author 半梦
 *
 */
public class EsException extends WebException{

	public EsException(String exception) {
		super(exception);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

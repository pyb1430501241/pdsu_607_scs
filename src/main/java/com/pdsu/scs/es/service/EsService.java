package com.pdsu.scs.es.service;

import com.pdsu.scs.exception.web.es.QueryException;

import java.util.List;

public interface EsService<T> {

	public List<T> queryByText(String text)throws QueryException;
	
}

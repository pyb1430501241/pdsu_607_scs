package com.pdsu.scs.es.service;

import java.util.List;

import com.pdsu.scs.exception.web.es.QueryException;

public interface EsService<T> {

	public List<T> queryByText(String text)throws QueryException;
	
}

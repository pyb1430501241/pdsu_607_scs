package com.pdsu.scs.es.dao;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.SearchHits;

import com.pdsu.scs.exception.web.es.QueryException;

public interface EsDao {

	public SearchHits queryBySearchRequest(SearchRequest request)throws QueryException;
	
}

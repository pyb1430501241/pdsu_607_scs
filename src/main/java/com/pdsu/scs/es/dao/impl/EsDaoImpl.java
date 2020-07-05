package com.pdsu.scs.es.dao.impl;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.es.QueryException;

/**
 * es 的 dao
 * @author 半梦
 *
 */
@Repository("esDao")
public class EsDaoImpl implements EsDao{

	@Autowired
	private  RestHighLevelClient restHighLevelClient;
	
	@Override
	public SearchHits queryBySearchRequest(SearchRequest request) throws QueryException{
		try {
			return restHighLevelClient.search(request, RequestOptions.DEFAULT).getHits();
		} catch (IOException e) {
			throw new QueryException("查询失败");
		}
	}
	
}

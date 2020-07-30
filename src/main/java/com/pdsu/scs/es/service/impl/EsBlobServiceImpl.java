package com.pdsu.scs.es.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pdsu.scs.bean.EsBlobInformation;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.es.service.EsService;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * es blob相关
 * @author 半梦
 *
 */
@Service("esBlobService")
public class EsBlobServiceImpl implements EsService<EsBlobInformation>{

	@Autowired
	private EsDao esDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EsBlobInformation> queryByText(String text) throws QueryException {
		BoolQueryBuilder bool = new BoolQueryBuilder();
		bool.should(QueryBuilders.matchQuery("title", text));
		bool.should(QueryBuilders.matchQuery("description", text));
		
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");
		highlightBuilder.field("description");
		highlightBuilder.preTags("<font color='red'>");
		highlightBuilder.postTags("</font>");
		
		
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(bool);
		builder.highlighter(highlightBuilder);
		
		SearchRequest request = new SearchRequest();
		
		request.indices("blob");
		request.source(builder);
		SearchHits hits = null;
		try {
			hits = esDao.queryBySearchRequest(request);
		} catch (QueryException e1) {
			throw new QueryException("查询博客失败");
		}
		SearchHit[] searchHits = hits.getHits();
		try {
			return (List<EsBlobInformation>) SimpleUtils.getObjectBySearchHit(searchHits, EsBlobInformation.class);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new QueryException("解析博客失败");
		}
	}

}

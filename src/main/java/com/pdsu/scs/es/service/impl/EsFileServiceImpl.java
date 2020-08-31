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

import com.pdsu.scs.bean.EsFileInformation;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.es.service.EsService;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.utils.SimpleUtils;

/**
 * es file 相关
 * @author 半梦
 *
 */
@Service("esFileService")
public class EsFileServiceImpl implements EsService<EsFileInformation>{

	@Autowired
	private EsDao esDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EsFileInformation> queryByText(String text) throws QueryException {
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
		
		request.indices("file");
		request.source(builder);
		SearchHits hits = null;
		try {
			hits = esDao.queryBySearchRequest(request);
		} catch (QueryException e1) {
			throw new QueryException("查询文件失败");
		}
		SearchHit[] searchHits = hits.getHits();
		try {
			return (List<EsFileInformation>) SimpleUtils.getObjectBySearchHit(searchHits, EsFileInformation.class);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			System.out.println(e.getMessage());
			throw new QueryException("解析文件失败");
		}
	}

	
}

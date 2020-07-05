package com.pdsu.scs.test;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@SpringJUnitConfig(locations = {"classpath:spring/spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class EsTest {
	
	
	@Autowired
	private  RestHighLevelClient restHighLevelClient;
	
	@Test
	public void  getTest() throws IOException {
		
		BoolQueryBuilder bool = new BoolQueryBuilder();
		bool.must(QueryBuilders.matchQuery("title", "java"));
		
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("username");
		highlightBuilder.preTags("<font color='red'>");
		highlightBuilder.postTags("</font>");
		
		
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(bool);
		builder.highlighter(highlightBuilder);
		
		SearchRequest request = new SearchRequest();
		
		request.indices("file");
		request.source(builder);
		
		SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		SearchHit[] hits = search.getHits().getHits();
		for(SearchHit hit : hits) {
			System.out.println(hit.getSourceAsMap());
		}
	}
		
}

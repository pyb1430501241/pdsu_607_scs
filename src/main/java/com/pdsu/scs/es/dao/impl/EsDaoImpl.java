package com.pdsu.scs.es.dao.impl;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pdsu.scs.bean.EsBlobInformation;
import com.pdsu.scs.bean.EsFileInformation;
import com.pdsu.scs.bean.EsUserInformation;
import com.pdsu.scs.es.dao.EsDao;
import com.pdsu.scs.exception.web.es.InsertException;
import com.pdsu.scs.exception.web.es.QueryException;
import com.pdsu.scs.exception.web.es.UpdateException;

/**
 * es 的 dao
 * @author 半梦
 *
 */
@Repository("esDao")
public class EsDaoImpl implements EsDao{

	@Autowired
	private  RestHighLevelClient restHighLevelClient;
	
	private static final Logger log = LoggerFactory.getLogger(EsDaoImpl.class);
	
	@Override
	public SearchHits queryBySearchRequest(SearchRequest request) throws QueryException{
		try {
			return restHighLevelClient.search(request, RequestOptions.DEFAULT).getHits();
		} catch (IOException e) {
			throw new QueryException("查询失败");
		}
	}
	
	@Override
	public Map<String, Object> queryByTableNameAndId(String str,Integer id) throws QueryException {
		GetRequest request = new GetRequest(str, id+"");
		try {
			GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
			return response.getSourceAsMap();
		} catch (IOException e) {
			throw new QueryException("查询失败");
		}
	}
	
	
	private boolean indexUtil(IndexRequest request) throws InsertException {
		IndexResponse response = null;
		try {
			response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
			ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
			if(shardInfo.getTotal() != shardInfo.getSuccessful()){
		            log.warn("处理成功的分片数少于总分片数!");
		    }
			return true;
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new InsertException("请求服务器失败");
		}
	}
	
	@Override
	public boolean insert(Object ob, Integer id) throws InsertException {
		IndexRequest request = null;
		if(ob instanceof EsUserInformation) {
			EsUserInformation user = (EsUserInformation) ob;
			request = new IndexRequest("user");
			request.id(id.toString());
			request.source(user.toString(), XContentType.JSON);
			return indexUtil(request);
		}else if(ob instanceof EsBlobInformation) {
			EsBlobInformation blob = (EsBlobInformation) ob;
			request = new IndexRequest("blob");
			request.id(id.toString());
			request.source(blob.toString(), XContentType.JSON);
			return indexUtil(request);
		}else if(ob instanceof EsFileInformation) {
			EsFileInformation file = (EsFileInformation) ob;
			request = new IndexRequest("file");
			request.id(id.toString());
			request.source(file.toString(), XContentType.JSON);
			return indexUtil(request);
		}else {
			throw new InsertException("类型转换异常");
		}
	}

	private boolean updateUtil(UpdateRequest request) throws UpdateException {
		UpdateResponse response = null;
		try {
			request.retryOnConflict(3);
			response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
			ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
			if(shardInfo.getTotal() != shardInfo.getSuccessful()){
		        log.warn("处理成功的分片数少于总分片数!");
		    }
			return true;
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new UpdateException("请求服务器失败");
		}
	}
	
	@Override
	public boolean update(Object ob, Integer id) throws UpdateException {
		UpdateRequest request = null;
		if(ob instanceof EsUserInformation) {
			EsUserInformation user = (EsUserInformation) ob;
			request = new UpdateRequest("user", id.toString());
			request.doc(user.toString(), XContentType.JSON);
			return updateUtil(request);
		}else if(ob instanceof EsBlobInformation) {
			EsBlobInformation blob = (EsBlobInformation) ob;
			request = new UpdateRequest("blob", id.toString());
			request.doc(blob.toString(), XContentType.JSON);
			return updateUtil(request);
		}else if(ob instanceof EsFileInformation) {
			EsFileInformation file = (EsFileInformation) ob;
			request = new UpdateRequest("file", id.toString());
			request.doc(file.toString(), XContentType.JSON);
			return updateUtil(request);
		}else {
			throw new UpdateException("类型转换异常");
		}
	}

}

package com.pdsu.scs.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * 配置 es 连接
 * @author Admin
 *
 */
public class RestHighLevelClientFactory {

	public RestHighLevelClient getRestHighLevelClient() {
		return new RestHighLevelClient(RestClient.builder(new HttpHost("129.204.206.237", 9200, "http")));
	}

}

/**
 * 
 */
package com.test.customdataindexr.services.httpclient;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import com.esreindexer.elasticsearch.client.services.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestHttpClient {
	@Test
	public void httpPost_PostToWorkingUrl_ReceiveOkayResponse() throws ClientProtocolException, IOException {
		HttpClient client = new HttpClient();
		HashMap<String,String> map = new HashMap();
		map.put("index_type","test");
		String json = new ObjectMapper().writeValueAsString(map);
		StringEntity entity = new StringEntity(json);
		CloseableHttpResponse response = client.post("http://localhost:9200/testindex", entity, true);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
	}

	@Test(expected=Throwable.class)
	public void httpPost_PostToNonWorkingUrl_ReceiveThrowable() throws ClientProtocolException, IOException {
		HttpClient client = new HttpClient();
		HashMap<String,String> map = new HashMap();
		map.put("index_type","test");
		String json = new ObjectMapper().writeValueAsString(map);
		StringEntity entity = new StringEntity(json);
		CloseableHttpResponse response = client.post("http://localhost:1200/testindex", entity, true);
	}
}
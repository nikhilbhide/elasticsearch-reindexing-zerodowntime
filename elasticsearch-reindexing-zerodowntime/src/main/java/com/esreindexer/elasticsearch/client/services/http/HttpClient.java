package com.esreindexer.elasticsearch.client.services.http;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
/**
 * HTTP client to invoke elastic search's rest services.
 * 
 * <p>
 * Though ES's java native client is used, Elastic search has not released/documented api such as _reindex, _query_for_update when it comes to native client.
 * Hence in order to use advanced features of ES, it is required to directly invoke rest api of ES.
 * 
 * @author nikhil.bhide
 * @since 1.0
 *
 */
public class HttpClient {
	/**
	 * Post api to post data to http method post
	 * @param url The url to which data is to be posted
	 * @param httpEntityInstance The instace of {@link HttpEntity}
	 * @param isJsonContent Indicator which indicates whether data is of type json
	 * @return CloseableHttpResponse The http response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public CloseableHttpResponse post(String url, HttpEntity httpEntityInstance,boolean isJsonContent) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		decorateHttpClient(isJsonContent,httpPost);
		httpPost.setEntity(httpEntityInstance);
		CloseableHttpResponse response = client.execute(httpPost);
		client.close();
		return response;
	}

	/**
	 * Decorates http header of post request with json parameters.
	 * @param isJsonContent Indicator which indicates whether data is of type json
	 * @param httpPost http post request object
	 */
	private void decorateHttpClient(boolean isJsonContent, HttpPost httpPost) {
		if(isJsonContent){
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
		}
	}
}
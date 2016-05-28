package com.esreindexer.elasticsearch.client.reindex.action;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.elasticsearch.index.reindex.ReindexResponse;
import com.esreindexer.elasticsearch.client.ElasticSearchClient;
import com.esreindexer.elasticsearch.client.reindex.json.entities.Dest;
import com.esreindexer.elasticsearch.client.reindex.json.entities.ReIndexRequest;
import com.esreindexer.elasticsearch.client.reindex.json.entities.Source;
import com.esreindexer.elasticsearch.client.services.http.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This class provides re-indexing capability using _reindex api.
 * 
 * <p>
 * Since 2.0, ES has been providing _reindex api. It is much efficient than scroll based api. 
 * This should be used for reindexing the index.
 * It provides lot of features such as cancelling reindexing task, overrride document (source/destination version), control conflicts etc..
 * 
 * @author nikhil.bhide
 * @since 1.0
 *
 */
public class ESBasedReindexAction {
	private ElasticSearchClient esClient;
	public ESBasedReindexAction(ElasticSearchClient client) {
		this.esClient = client;		
	}
	
	/**
	 * Reindexes old index to new index using reindex api.
	 *
	 * @param ReIndexingRequest The reindexing request
	 * @return ReindexResponse the reindex response
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ReindexResponse reindexUsingRest(ReIndexingRequest reIndexingRequest) throws ClientProtocolException, IOException {
		ReIndexRequest request = new ReIndexRequest();
		ReindexResponse response = new ReindexResponse();
		Source source = new Source();
		source.setIndex(reIndexingRequest.getOldIndex());
		Dest destination = new Dest();
		destination.setIndex(reIndexingRequest.getNewIndex());
		request.setDest(destination);
		request.setSource(source);
		String json = new ObjectMapper().writeValueAsString(request);
		StringEntity entity = new StringEntity(json);
		HttpClient httpClient = new HttpClient();
		response = (ReindexResponse) httpClient.post("http://localhost:9200/testindex".concat("_/reindex"), entity, true);
		return response;
	}
	
	/**
	 * Reindexes old index to new index using _reindex api.
	 *
	 * @param ReIndexingRequest The reindexing request
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public ReindexResponse reindex(ReIndexingRequest reIndexingRequest) throws ClientProtocolException, IOException {
		ReindexRequestBuilder builder = ReindexAction.INSTANCE.
				newRequestBuilder(esClient.getClient())
				.source(reIndexingRequest.getOldIndex())
				.destination(reIndexingRequest.getNewIndex())
				.size(reIndexingRequest.getSize())
				.timeout(reIndexingRequest.getKeepAlive());
		ReindexResponse response = builder.get();
		return response;
	}
}
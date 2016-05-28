package com.esreindexer.elasticsearch.client.reindex.action;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;

import com.esreindexer.elasticsearch.client.ElasticSearchClient;
import com.esreindexer.elasticsearch.client.bulk.action.BulkProcessorLoggerLister;

/**
 * This class provides re-indexing capability using scroll.
 * 
 * <p>
 * Scroll API of ElasicSearch can be used to retrieve large numbers of results (or even all results) from a single search request,
 * in much the same way as you would use a cursor on a traditional database.
 * ES released a new api named as 
 * This api should be used instead of scroll based re-indexing implementation.
 * 
 * @author nikhil.bhide
 * @since 1.0
 *
 */

public class ScrollBasedReIndexAction {
	private ElasticSearchClient esClient;

	public ScrollBasedReIndexAction(ElasticSearchClient client) {
		this.esClient = client;		
	}

	/**
	 * Reindexes old index to new index using implementaion based on scroll.
	 *
	 * @param ReIndexingRequest 
	 */
	public ReindexingResponse reindex(ReIndexingRequest reIndexingRequestrequest) {
		long startTime = System.currentTimeMillis();
		ReindexingResponse response = new ReindexingResponse();
		response.setStartTime(System.currentTimeMillis());
		org.elasticsearch.action.search.SearchResponse searchResponse =  esClient.getClient()
				.prepareSearch(reIndexingRequestrequest.getOldIndex())
				.setTypes(Strings.EMPTY_ARRAY)
				.setQuery(reIndexingRequestrequest.getBody())
				.setSearchType(SearchType.SCAN)
				.setScroll(new Scroll(reIndexingRequestrequest.getKeepAlive()))
				.setSize(reIndexingRequestrequest.getSize())
				.execute()
				.actionGet();
		BulkProcessor bulkProcessor = BulkProcessor.builder(esClient.getClient(), new BulkProcessorLoggerLister())
				.setConcurrentRequests(100)
				.build();
		try {
			while (true) {
				for (SearchHit hit : searchResponse.getHits()) {
					IndexRequest indexingRequest = new IndexRequest(reIndexingRequestrequest.getNewIndex(), hit.type(), hit.id());
					indexingRequest.source(hit.source());
					bulkProcessor.add(indexingRequest);
				}
				searchResponse =  esClient.getClient().prepareSearchScroll(searchResponse.getScrollId()).setScroll(new Scroll(new TimeValue(60000))).execute().actionGet();
				if (searchResponse.getHits().getHits().length == 0) 
				{//Break condition: No hits are returned
					System.exit(0);
					bulkProcessor.close();
					break;
				}
			}
		}
		catch (Throwable failure){
			response.setFailure(failure);
			response.setHasFailures(true);
		}
		finally {
			response.setEndTime(System.currentTimeMillis());
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken is" +(endTime-startTime));
		return response;
	}
}
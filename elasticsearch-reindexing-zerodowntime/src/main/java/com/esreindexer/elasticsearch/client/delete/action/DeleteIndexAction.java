package com.esreindexer.elasticsearch.client.delete.action;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import com.esreindexer.elasticsearch.client.ElasticSearchClientMediator;

/**
 * Perform operations related to deletion/removal of ES entities
 * 
 * @author nikhil.bhide
 * @since 1.0
 */
public class DeleteIndexAction {
	ElasticSearchClientMediator esClientMediator;

	/**
	 * Constructor 
	 * 
	 * @param elasticSearchClientMediator The elasticSearchClient
	 */
	public DeleteIndexAction(ElasticSearchClientMediator elasticSearchClientMediator) {
		this.esClientMediator = elasticSearchClientMediator;
	}

	/**
	 * Deletes index
	 * @param index The name of index to be deleted
	 */
	public boolean deleteIndex(String index) {
		DeleteIndexResponse deleteResponse = esClientMediator
				.getESClient()
				.getClient()
				.admin()
				.indices()
				.delete(new DeleteIndexRequest(index)).actionGet();
		return deleteResponse.isAcknowledged();
	}
}
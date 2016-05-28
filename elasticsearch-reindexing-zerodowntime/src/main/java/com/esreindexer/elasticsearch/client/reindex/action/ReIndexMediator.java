package com.esreindexer.elasticsearch.client.reindex.action;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.elasticsearch.index.reindex.ReindexResponse;
import com.esreindexer.elasticsearch.client.ElasticSearchClientMediator;

/**
 * The mediator which acts as an intermediatory between client class and classes that provide reindexing implementation.
 * <p>
 * There are two implementations of reindexing
 * 1. Scroll based reindexing - Scrolling is like cursor of database. Client opens the scroll and reindexing is done based on scroll id.
 * 	  Client has to manage eveything right from opening scolling, batching up the request and ensuring that reindexing is complete.
 * 2. ES based reindexing - Since 2.0 version, ES has been proving _reindex api which does reindexing. It is a module/pluging that needs to be added on client side.
 * 	  Internally, it uses scroll to do the reindexing. But it provides additional features such version conflicts, version override (source/destination) and managing indexing failures/search failures.  
 * 	 
 * If you are using ES 2.X version then it is adivisable to switch to _reindex api of elastic search
 * 
 * @author nikhil.bhide
 * @since 1.0
 *
 */
public class ReIndexMediator {
	private ElasticSearchClientMediator esClientMediator;
	private ESBasedReindexAction esBasedReIndexAction;
	private ScrollBasedReIndexAction scrollBasedReIndexAction;

	/**
	 * Constructor
	 * 
	 * @param client The ElasticSearchClientMediator client
	 */
	public ReIndexMediator(ElasticSearchClientMediator client) {
		this.esClientMediator = client;
		esBasedReIndexAction = new ESBasedReindexAction(esClientMediator.getESClient());
		scrollBasedReIndexAction = new ScrollBasedReIndexAction(esClientMediator.getESClient());
	}


	/**
	 * Reindexes es data from old index to new index.
	 * 
	 * @param reIndexingMethod Methodology to use for reindexing. 1. ES based _reindex api 2.Scroll based es api
	 * @param oldIndex The name of old index
	 * @param newIndex The name of new index
	 * @param deleteOldIndex Indicator which indicates whether to delete old index after reindexing operation 
	 * @param oldIndexAlias Alias pointing to old index
	 * @param pointAliasToNewIndex Indicator which indicates whether to point alias to new index and remove alias from old index
	 * @param rollbackChangesUponFailure Indicates whether to delete new index in case of reindexing failure
	 * @return Reindexing response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Object reindex(int reIndexingMethod,String oldIndex,String newIndex,boolean deleteOldIndex,String oldIndexAlias,boolean pointAliasToNewIndex, boolean rollbackChangesUponFailure) throws ClientProtocolException, IOException {
		ReIndexingRequest request = new ReIndexingRequest(oldIndex, newIndex);
		request.setOldIndex(oldIndex);
		request.setNewIndex(newIndex);
		request.setRollbackUponFailure(rollbackChangesUponFailure);
		switch(reIndexingMethod) {
		case 1:
			ReindexResponse reindexResponse = esBasedReIndexAction.reindex(request);
			if(reindexResponse.getIndexingFailures()!=null && reindexResponse.getIndexingFailures().size()==0) {
				postSuccessfulReindexing(oldIndex,newIndex,pointAliasToNewIndex, oldIndexAlias,deleteOldIndex);
			}
			else {
				rollBackChanges(newIndex);
			}
			return reindexResponse;
		case 2:
			ReindexingResponse reIndexingResponse = scrollBasedReIndexAction.reindex(request);
			rollBackChanges(newIndex);
			return reIndexingResponse;
		default:
			throw new IllegalArgumentException("Value of reIndexingMethod is incorrect. 1. ES based ReIndexing 2. Scroll Based ReIndexing");
		}
	}
	/**
	 * Rollback changes, delete newly created index.
	 *  
	 * @param newIndex The name of new index
	 */
	private void rollBackChanges(String newIndex) {
		boolean exists = esClientMediator.getESClient()
				.getClient()
				.admin()
				.indices()
				.prepareExists(newIndex)
				.execute()
				.actionGet()
				.isExists();
		if(exists) {
			boolean isDeleteSuccess = esClientMediator.deleteIndex(newIndex);
			if(!isDeleteSuccess)
				throw new RuntimeException("Reindexing operation failed. Can not delete newly created index");				
		}
	}	

	/**
	 * Post action of reindexing operation
	 * @param pointAliasToNewIndex Indicator which indicates whether to point alias to new index and remove alias from old index
	 * @param rollbackChangesUponFailure Indicates whether to delete new index in case of reindexing failure
	 */
	private void postSuccessfulReindexing(String oldIndex, String newIndex,boolean pointAliasToNewIndex, String oldIndexAlias, boolean deleteOldIndex) {
		if(pointAliasToNewIndex) {
			esClientMediator.pointAliastoNewIndex(oldIndexAlias, newIndex, oldIndex);
		}
		if(deleteOldIndex) {
			boolean isDeleted = esClientMediator.deleteIndex(oldIndex);
			if(!isDeleted) {
				throw new RuntimeException("Old Index could not be deleted");
			}
		}			
	}
}
/**
 * 
 */
package com.esreindexer.elasticsearch.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;

import com.esreindexer.elasticsearch.client.alias.action.AliasAction;
import com.esreindexer.elasticsearch.client.delete.action.DeleteIndexAction;
import com.esreindexer.elasticsearch.client.reindex.action.ESBasedReindexAction;
import com.esreindexer.elasticsearch.client.reindex.action.ReIndexMediator;
import com.esreindexer.elasticsearch.client.reindex.action.ScrollBasedReIndexAction;

/**
 * Mediator class which provides provides access to all elastic search operations like one stop for all operations.
 * This class should be used to perform different operations on Elastic Search be its indexing, reindexing, search i.e any crud operations
 * 
 * <p>
 * In general case, it is required to access different classes to perform different operations on Elastic Search.
 * For example, {@link ESBasedReindexAction} or {@link ScrollBasedReIndexAction} to access reindexing operations or {@link DeleteIndexAction} to perform delete operations on ES.
 * Same is applicable to indexing and search operations.
 *   
 *	
 * @author nikhil.bhide
 * @since 1.0
 *
 */
public class ElasticSearchClientMediator {
	private ElasticSearchClient esClient = null;
	private ReIndexMediator reIndexMediator= null;
	private DeleteIndexAction deleteIndexAction = null;
	private AliasAction aliasAction = null;

	/**
	 * @return the client
	 */
	public ElasticSearchClient getESClient() {
		return esClient;
	}

	/**
	 * Creates Elastic Search transport client based on the input parameters - host and port. 
	 *
	 * @param host The hostname/ipaddess of the elastic search server 
	 * @param port The port on which Elastic search service is running on
	 * @param masterNodeHttpUrl Elastic Search http url of master node
	 * @throws UnknownHostException 
	 */
	public ElasticSearchClientMediator(String host, int tcpPort, String masterNodeHttpUrl) throws UnknownHostException {
		esClient = new ElasticSearchClient();
		boolean isClientCreationSuccess = esClient.initClient(host, tcpPort, masterNodeHttpUrl);
		if(!isClientCreationSuccess) 
			throw new RuntimeException("Issue in creating transport client of elastic search with provided parameters - Host: "+host + "and tcp port :" + tcpPort); 
		reIndexMediator = new ReIndexMediator(this);
		deleteIndexAction = new DeleteIndexAction(this);
		aliasAction = new AliasAction(this);
	}

	/**
	 * see {@link ReIndexMediator#reindex(int, String, String, boolean, String, boolean, boolean)}
	 */
	public Object reindex(int reIndexingMethod,String oldIndex,String newIndex,boolean deleteOldIndex,String oldIndexAlias,boolean pointAliasToNewIndex, boolean rollbackChangesUponFailure) throws ClientProtocolException, IOException {
		return reIndexMediator.reindex(reIndexingMethod, oldIndex, newIndex, deleteOldIndex, oldIndexAlias, pointAliasToNewIndex, rollbackChangesUponFailure); 
	}

	/**
	 * see {@link DeleteIndexAction#deleteIndex(String)
	 */
	public boolean deleteIndex(String indexName) {
		return deleteIndexAction.deleteIndex(indexName);
	}
	
	/**
	 * see {@link AliasAction#addAliastoIndex(String, String)
	 */
	public IndicesAliasesResponse addAliastoIndex(String aliasName, String indexName) {
		return aliasAction.addAliastoIndex(aliasName, indexName);
	}

	/**
	 * see {@link AliasAction#removeAliasFromIndex(String, String)}
	 */
	public IndicesAliasesResponse removeAliasFromIndex(String aliasName, String indexName){
		return aliasAction.removeAliasFromIndex(aliasName, indexName);
	}
	
	/**
	 * see {@link AliasAction#removeAliasFromIndex(String, String)}
	 */
	public boolean isAliasExistForGivenIndex (String alias, String indexName) throws InterruptedException, ExecutionException {
		return aliasAction.hasAliases(new String[] {alias}, new String[] {indexName});
	}
	
	/**
	 * see {@link AliasAction#pointAliastoNewIndex(String, String, String)}
	 */
	public IndicesAliasesResponse pointAliastoNewIndex(String aliasName,String newIndex, String oldIndex){
		return aliasAction.pointAliastoNewIndex(aliasName, newIndex,oldIndex);
	}
}

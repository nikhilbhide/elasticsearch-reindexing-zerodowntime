/**
 * 
 */
package com.esreindexer.elasticsearch.client.alias.action;

import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.client.Requests;
import com.esreindexer.elasticsearch.client.ElasticSearchClientMediator;

/**
 * Interface for all operations related to alias.
 * 
 * <p>
 * What is the use of alias?
 * Alias must be used as it highly helps during reindex operation. Aliases can be switched in between indexes and deletion or addition of alias is atomic operation.
 * Alias removes tight coupling of application with the index name.
 * Indexes can be removed at any time still application can continue running without any problem.
 * There should be read_alias and write_alias.
 * read_aliases allow to read data from different index where write_alias allows to write data to only ond index. 
 *  
 * @author nikhil.bhide
 * @since 1.0
 */
public class AliasAction {
	private ElasticSearchClientMediator esClientMediator;

	public AliasAction(ElasticSearchClientMediator elasticSearchClientMediator) {
		this.esClientMediator = elasticSearchClientMediator;
	}
	/**
	 * Add alias to index
	 * @param aliasName The name of alias
	 * @param indexName The name of index to which alias to be added
	 * @return IndicesAliasesResponse The IndicesAliasesResponse
	 */
	public IndicesAliasesResponse addAliastoIndex(String aliasName, String indexName) {
		return esClientMediator.getESClient()
				.getClient().
				admin().
				indices().
				prepareAliases().
				addAlias(indexName, aliasName).
				execute().
				actionGet();
	}

	/**
	 * Remove alias from index
	 * @param aliasName The name of alias
	 * @param indexName The name of index from which alias to be removed
	 */
	public IndicesAliasesResponse removeAliasFromIndex(String aliasName, String indexName){
		return esClientMediator
				.getESClient()
				.getClient()
				.admin()
				.indices()
				.prepareAliases()
				.removeAlias(indexName, aliasName)
				.execute()
				.actionGet();
	}

	/**
	 * Remove alias from index
	 * @param aliasName The name of alias
	 * @param newIndex The name of new index to which alias to be attached
	 * @param oldIndex The name of old index from which alias to be detached
	 * @return IndicesAliasesResponse The IndicesAliasesResponse
	 */
	public IndicesAliasesResponse pointAliastoNewIndex(String aliasName,String newIndex, String oldIndex){
		return esClientMediator.getESClient()
				.getClient()
				.admin()
				.indices()
				.prepareAliases()
				.addAlias(newIndex, aliasName)
				.removeAlias(oldIndex, aliasName)
				.execute()
				.actionGet();
	}

	/**
	 * Checks if at least one of the specified aliases exists in the specified concrete indices. Wildcards are supported in the
	 * alias names for partial matches.
	 *
	 * @param aliases The names of the index aliases to find
	 * @param concreteIndices The concrete indexes the index aliases must point to order to be returned.
	 * @return whether at least one of the specified aliases exists in one of the specified concrete indices.
	 */
	public boolean hasAliases(String[] aliases, String[] indices) throws InterruptedException, ExecutionException {
		return esClientMediator.getESClient().getClient().admin().cluster()
				.state(Requests.clusterStateRequest())
				.actionGet()
				.getState()
				.getMetaData()
				.hasAliases(aliases, indices);
	}
}
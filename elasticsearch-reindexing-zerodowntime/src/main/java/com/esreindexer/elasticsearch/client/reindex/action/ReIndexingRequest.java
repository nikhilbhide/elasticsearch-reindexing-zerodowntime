package com.esreindexer.elasticsearch.client.reindex.action;

import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Request of Reindexing action
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
public class ReIndexingRequest {
	private String oldIndex;
	private String newIndex;
	private QueryBuilder body = QueryBuilders.matchAllQuery();
	private TimeValue keepAlive = new TimeValue(60000);
	private int size = 500;
	private int concurrentRequests;
	private boolean rollbackUponFailure;
	private boolean suppressFailure;
	private String[] types;
	
	public ReIndexingRequest(String oldIndex,String newIndex) {
		setOldIndex(oldIndex);
		setNewIndex(newIndex);
	}

	/**
	 * @return the oldIndex
	 */
	public String getOldIndex() {
		return oldIndex;
	}

	/**
	 * @param oldIndex the oldIndex to set
	 */
	public void setOldIndex(String oldIndex) {
		if(oldIndex==null || oldIndex.isEmpty()) 
			throw new IllegalArgumentException("Value of old index can not be null or empty");
		this.oldIndex = oldIndex;
	}

	/**
	 * @return the newIndex
	 */
	public String getNewIndex() {
		return newIndex;
	}

	/**
	 * @param newIndex the newIndex to set
	 */
	public void setNewIndex(String newIndex) {
		if(newIndex==null || newIndex.isEmpty())
			throw new IllegalArgumentException("Value of new index can not be null or empty");
		this.newIndex = newIndex;
		
	}

	/**
	 * @return the body
	 */
	public QueryBuilder getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(QueryBuilder body) {
		this.body = body;
	}

	/**
	 * @return the keepAlive
	 */
	public TimeValue getKeepAlive() {
		return keepAlive;
	}

	/**
	 * @param keepAlive the keepAlive to set
	 */
	public void setKeepAlive(TimeValue keepAlive) {
		this.keepAlive = keepAlive;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the concurrentRequests
	 */
	public int getConcurrentRequests() {
		return concurrentRequests;
	}

	/**
	 * @param concurrentRequests the concurrentRequests to set
	 */
	public void setConcurrentRequests(int concurrentRequests) {
		this.concurrentRequests = concurrentRequests;
	}

	/**
	 * @return the rollbackUponFailure
	 */
	public boolean isRollbackUponFailure() {
		return rollbackUponFailure;
	}

	/**
	 * @param rollbackUponFailure the rollbackUponFailure to set
	 */
	public void setRollbackUponFailure(boolean rollbackUponFailure) {
		this.rollbackUponFailure = rollbackUponFailure;
	}

	/**
	 * @return the suppressFailure
	 */
	public boolean isSuppressFailure() {
		return suppressFailure;
	}

	/**
	 * @param suppressFailure the suppressFailure to set
	 */
	public void setSuppressFailure(boolean suppressFailure) {
		this.suppressFailure = suppressFailure;
	}

	/**
	 * @return the types
	 */
	public String[] getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String[] types) {
		this.types = types;
	}
}
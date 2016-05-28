/**
 * 
 */
package com.esreindexer.elasticsearch.client.reindex.action;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response of reindexing action
 * 
 * @author nikhil.bhide
 * @since 1.0
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReindexingResponse {
	private boolean hasFailures;
	private Throwable failure;
	private String responseMessage;
	private long startTime;
	private long endTime;
	
	/**
	 * @return the hasFailures
	 */
	public boolean hasFailures() {
		return getFailure()!=null;
	}
	/**
	 * @param hasFailures the hasFailures to set
	 */
	public void setHasFailures(boolean hasFailures) {
		this.hasFailures = hasFailures;
	}
	/**
	 * @return the failure
	 */
	public Throwable getFailure() {
		return failure;
	}
	/**
	 * @param list the failure to set
	 */
	public void setFailure(Throwable throwable) {
		this.failure = throwable;
	}
	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}
	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}
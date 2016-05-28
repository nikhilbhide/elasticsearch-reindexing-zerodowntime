package com.esreindexer.elasticsearch.client.bulk.action;

import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * Logger based BulkProcessorListerner which implements {@link Listener} 
 * It implements call back apis that are invoked when bulk request is executed 
 * 
 * @author nikhil.bhide
 * @since 1.0
 * 
 */
public class BulkProcessorLoggerLister implements Listener {

	/* (non-Javadoc)
	 * @see org.elasticsearch.action.bulk.BulkProcessor.Listener#beforeBulk(long, org.elasticsearch.action.bulk.BulkRequest)
	 */
	@Override
	public void beforeBulk(long executionId, BulkRequest request) {
		System.out.println("Bulk Going to execute new bulk composed of {} actions" + request.numberOfActions());		
	}

	/* (non-Javadoc)
	 * @see org.elasticsearch.action.bulk.BulkProcessor.Listener#afterBulk(long, org.elasticsearch.action.bulk.BulkRequest, org.elasticsearch.action.bulk.BulkResponse)
	 */
	@Override
	public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
		System.out.println("Executed bulk composed of " + request.numberOfActions() + "actions");		
	}

	/* (non-Javadoc)
	 * @see org.elasticsearch.action.bulk.BulkProcessor.Listener#afterBulk(long, org.elasticsearch.action.bulk.BulkRequest, java.lang.Throwable)
	 */
	@Override
	public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
		System.out.println("Error in executing bulk"+ failure);
		new RuntimeException(failure);		
	}
}

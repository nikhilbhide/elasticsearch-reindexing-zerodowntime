package com.esreindexer.elasticsearch.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.reindex.ReindexPlugin;

/**
 * Elastic Search Client class.
 *  
 * @author nikhil.bhide
 * @since 1.0
 */
public class ElasticSearchClient {
	private Client client;
	private String esMasterNodeHttpUrl;

	/**
	 * Getter for TransportClient
	 * @return Client The TransportClient
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Creates Elastic Search transport client based on the input parameters - host and port. 
	 * It assumes that cluster name is es_cluster.
	 * Returns true if transportClient is successfully created.
	 * 
	 * <p>
	 * What is the use of "client.transport.sniff"?
	 * It allows transport client to dynamically add new hosts and remove old ones.
	 * When sniffing is enabled then at fitst, transport client will connect to the nodes in its internal node list, which is built via calls to addTransportAddress. 
	 * After this, the client will call the internal cluster state API on those nodes to discover available data nodes.The internal node list of the client will be replaced with those data nodes only. 
	 * This list is refreshed every five seconds by default. 
	 * Note that the IP addresses the sniffer connects to are the ones declared as the publish address in those node’s elasticsearch config.
	 * 
	 * @param host The hostname/ipaddess of the elastic search server 
	 * @param port The port on which Elastic search service is running on
	 * @param masterNodeHttpUrl Elastic Search http url of master node
	 * @return <<tt>>true<<tt>> if successful transport client creation
	 * @throws UnknownHostException
	 */
	public boolean initClient(String host, int tcpPort, String masterNodeHttpUrl) throws UnknownHostException {
		Settings settings = Settings.settingsBuilder()
			    .put("client.transport.sniff", true)
			    .put("cluster.name", "es_cluster").build();
		client = TransportClient.builder()
				.settings(settings)
				.addPlugin(ReindexPlugin.class)
				.build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),tcpPort));
		setESMasterNodeUrl(masterNodeHttpUrl);
		return client!=null;
	}
	
	/**
	 * @return the eSMasterNodeUrl
	 */
	public String getESMasterNodeUrl() {
		return esMasterNodeHttpUrl;
	}

	/**
	 * @param masterNodeHttpUrl the eSMasterNodeUrl to set
	 */
	public void setESMasterNodeUrl(String masterNodeHttpUrl) {
		esMasterNodeHttpUrl = masterNodeHttpUrl;
	}
	
	/**
	 * This method indexes document in elastic search. Document is in json format.
	 * Returns true if client can successfully creates a new index 
	 * @param indexName The name of the index
	 * @param indexType Thr type of the index
	 * @param json The document to be indexed
	 * @param numRetries The number of retries in case of error for fault tolerance
	 * @return <<tt>>true<<tt>> if successful document creation
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public boolean indexSingle(String indexName, String indexType, String json, int numRetries) throws InterruptedException, ExecutionException {
		IndexResponse response = client.prepareIndex(indexName,indexType)
				.setSource(json).execute()
				.actionGet();
		
		return response!=null;
	}

	/**
	 * Close conection
	 */
	public void close() {
		client.close();		
	}	
}	

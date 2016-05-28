package com.reindexing.test.elasticsearch.reindex.action;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import org.apache.http.client.ClientProtocolException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.reindex.ReindexResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import com.esreindexer.elasticsearch.client.ElasticSearchClientMediator;
import com.esreindexer.elasticsearch.client.reindex.action.ReIndexMediator;
import com.esreindexer.elasticsearch.client.reindex.action.ReindexingResponse;
import com.reindexing.test.type.IntegrationTest.IntegrationTest;

@Category(IntegrationTest.class)
public class TestESClientReIndexAction {
	private static ElasticSearchClientMediator clientMediator;
	private static String oldIndexReindex = "oldindexreindex";
	private static String newindexESbasedReindex = "newindexesbasedreindex";
	private static String newindexScrollbasedReindex = "newindexscrollbasedreindex";
	private static String newindexWithAliasAfterReindex = "newindexwithaliasreindex";
	private ReIndexMediator reIndexMediator = new ReIndexMediator(clientMediator);
	
	@BeforeClass
	public static void setup() throws UnknownHostException {
		clientMediator = new ElasticSearchClientMediator("localhost", 9300, "http://localhost:9200");
		String json = "{" +
				"\"user\":\"kimchy\"," +
				"\"postDate\":\"2013-01-30\"," +
				"\"message\":\"trying out Elasticsearch\"" +
				"}";
		IndexResponse response = clientMediator.getESClient()
				.getClient()
				.prepareIndex(oldIndexReindex, oldIndexReindex)
				.setSource(json)
				.get();
		assertTrue(response.isCreated());
	}

	@Test
	public void esBasedReindexing_oldIndexToNewIndex_Success() throws InterruptedException, ExecutionException, ClientProtocolException, IOException {
		ReindexResponse reIndexingResponse = (ReindexResponse) reIndexMediator.reindex(1, oldIndexReindex, newindexESbasedReindex, false, null, false, true);
		assertTrue(reIndexingResponse.getIndexingFailures().isEmpty());
	}

	@Test
	public void scrollBasedReindexing_oldIndexToNewIndex_Success() throws InterruptedException, ExecutionException, ClientProtocolException, IOException {
		ReindexingResponse reIndexingResponse = (ReindexingResponse) reIndexMediator.reindex(2, oldIndexReindex, newindexScrollbasedReindex, false, null, false, true);
		assertTrue(!reIndexingResponse.hasFailures());
	}

	public void reindex_deleteOldIndexAfterReindexing_Success() throws ClientProtocolException, IOException {
		String oldIndex = "oldindexreindex";
		String newIndex = "newindexreindex";
		String json = "{" +
				"\"user\":\"kimchy\"," +
				"\"postDate\":\"2013-01-30\"," +
				"\"message\":\"trying out Elasticsearch\"" +
				"}";

		IndexResponse response = clientMediator
				.getESClient()
				.getClient()
				.prepareIndex("oldIndexToBeDeleted", "type")
				.setSource(json)
				.get();
		ReindexResponse reIndexingResponse = (ReindexResponse) reIndexMediator.reindex(1, oldIndex, newIndex, true, null, false, true);
		Boolean oldIndexexists = clientMediator
				.getESClient()
				.getClient()
				.admin()
				.indices()
				.prepareExists(oldIndex)
				.execute().actionGet().isExists();
		assertTrue(!oldIndexexists);
		clientMediator.deleteIndex(newIndex);
	}

	@Test
	public void reindex_pointAliasToNewIndexAfterReindex_Success() throws ClientProtocolException, IOException, InterruptedException, ExecutionException{
		clientMediator.addAliastoIndex("oldIndexAlias",oldIndexReindex);
		ReindexResponse reIndexingResponse = (ReindexResponse) reIndexMediator.reindex(1, oldIndexReindex, newindexWithAliasAfterReindex, false, "oldIndexAlias", true, true);
		assertTrue(reIndexingResponse.getIndexingFailures().isEmpty());
		assertTrue(clientMediator.isAliasExistForGivenIndex("oldIndexAlias",newindexWithAliasAfterReindex));
		assertTrue(!clientMediator.isAliasExistForGivenIndex("oldIndexAlias",oldIndexReindex));
	}

	@AfterClass
	public static void tearDown() {
		clientMediator.deleteIndex(oldIndexReindex);
		clientMediator.deleteIndex(newindexESbasedReindex);
		clientMediator.getESClient().close();
	}
}
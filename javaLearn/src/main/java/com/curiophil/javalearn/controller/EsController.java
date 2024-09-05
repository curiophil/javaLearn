package com.curiophil.javalearn.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shadow.org.elasticsearch.common.collect.List;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/es")
public class EsController {


    @Resource
    JdbcTemplate esTemplate;

    @GetMapping("/sql")
    public Object sql(@RequestParam String sql) {
//        return esTemplate.queryForList(sql);
        RestClient restClient = null;
        ElasticsearchTransport transport = null;
        try {
            RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = httpClientBuilder ->
                    httpClientBuilder
                            // this request & response header manipulation helps get around newer (>=7.16) versions
                            // of elasticsearch-java client not working with older (<7.14) versions of Elasticsearch
                            // server
                            .setDefaultHeaders(
                                    List.of(
                                            new BasicHeader(
                                                    HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())))
                            .addInterceptorLast(
                                    (HttpResponseInterceptor)
                                            (response, context) ->
                                                    response.addHeader("X-Elastic-Product", "Elasticsearch"));


            restClient = RestClient
                    .builder(new HttpHost("10.253.171.224", 30269))
                    .setHttpClientConfigCallback(httpClientConfigCallback)
                    .setPathPrefix("/api/elasticsearch")
                    .build();



            transport = new RestClientTransport(
                    restClient, new JacksonJsonpMapper());


            ElasticsearchClient client = new ElasticsearchClient(transport);

//            QueryResponse queryResponse = client.sql().query(builder -> builder.query(sql));
//            return queryResponse.rows();

            SearchResponse<Object> queryResponse = client.search(_0 -> _0.index("314_ecloud_inventory_ecso_log_info1_deflector")
                    .sort(_1 -> _1.field(_2 -> _2.order(SortOrder.Desc).field("@timing"))), Object.class);
            return queryResponse.hits().hits();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                transport.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                restClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
//        String address = "jdbc:es://" + "http://*:30269/api/elasticsearch";
        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = httpClientBuilder ->
                httpClientBuilder
                        // this request & response header manipulation helps get around newer (>=7.16) versions
                        // of elasticsearch-java client not working with older (<7.14) versions of Elasticsearch
                        // server
                        .setDefaultHeaders(
                                List.of(
                                        new BasicHeader(
                                                HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())))
                        .addInterceptorLast(
                                (HttpResponseInterceptor)
                                        (response, context) ->
                                                response.addHeader("X-Elastic-Product", "Elasticsearch"));


        RestClient restClient = RestClient
                .builder(new HttpHost("10.253.171.224", 30269))
                .setHttpClientConfigCallback(httpClientConfigCallback)
                .setPathPrefix("/api/elasticsearch")
                .build();



        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());


        ElasticsearchClient client = new ElasticsearchClient(transport);
/*        PutIndicesSettingsResponse putIndicesSettingsResponse = client.indices()
                .putSettings(_0 -> _0
                        .index("326_fn-web-env-stdout_127")
                        .settings(_1 -> _1
                                .otherSettings("index.routing.allocation.require.box_type", JsonData.of("cold"))
                        )
                );
        System.out.println(putIndicesSettingsResponse.acknowledged());*/

/*        ScrollResponse<Object> scroll = client.scroll(_0 -> _0
                        .scroll(_1 -> _1
                                .time("10m")
                        )
                        .scrollId("FGluY2x1ZGVfY29udGV4dF91dWlkDnF1ZXJ5VGhlbkZldGNoBRZETVp5WVF2T1JCdXRFTHFwZzlPMzVnAAAAAAArFL4Wb2FIQnREYUFRV2FvLURHT09CbWtYQRY4VkZyMmk3NFNsSzdhN2ZNTWdwR2NBAAAAAAAzm9kWUEw1RXlMTmpUV2FzZU1rSnd6ZEMyZxZETVp5WVF2T1JCdXRFTHFwZzlPMzVnAAAAAAArFL8Wb2FIQnREYUFRV2FvLURHT09CbWtYQRY4VkZyMmk3NFNsSzdhN2ZNTWdwR2NBAAAAAAAzm94WUEw1RXlMTmpUV2FzZU1rSnd6ZEMyZxZETVp5WVF2T1JCdXRFTHFwZzlPMzVnAAAAAAArFMIWb2FIQnREYUFRV2FvLURHT09CbWtYQQ=="),
                Object.class);
        scroll.hits().hits().stream().forEach(System.out::println);*/


        BulkListener<String> listener = new BulkListener<String>() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request, java.util.List<String> contexts) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, java.util.List<String> contexts, BulkResponse response) {
                // The request was accepted, but may contain failed items.
                // The "context" list gives the file name for each bulk item.
                for (int i = 0; i < contexts.size(); i++) {
                    BulkResponseItem item = response.items().get(i);
                    if (item.error() != null) {
                        // Inspect the failure cause
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, java.util.List<String> contexts, Throwable failure) {
                // The request could not be sent
            }
        };

        RestHighLevelClient restHighLevelClient = new RestHighLevelClientBuilder(restClient).setApiCompatibilityMode(true).build();
        BulkProcessor bulkProcessor = BulkProcessor.builder(((bulkRequest, bulkResponseActionListener) -> {
            restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, bulkResponseActionListener);
        }), new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest) {

            }

            @Override
            public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, org.elasticsearch.action.bulk.BulkResponse bulkResponse) {

            }

            @Override
            public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, Throwable throwable) {

            }
        }).setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueMillis(1000), 10)).build();
        bulkProcessor.add(new IndexRequest("x").source(XContentType.JSON, "{}"));



        BulkIngester<String> ingester = BulkIngester.of(b -> b
                .client(client)
                .maxOperations(10000)
                .maxConcurrentRequests(5)
                .maxSize(10*1024*1024)
                .listener(listener)
//                .flushInterval(1, TimeUnit.SECONDS)
        );


        ingester.add(op -> op
                .index(idx -> idx
                        .index("logs")
                        .document(".......................")
                )
        );

        bulkProcessor.awaitClose(5, TimeUnit.SECONDS);
        ingester.close();
        transport.close();
    }


}

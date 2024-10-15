package com.cuiriophil.webflux.controller;


import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.WebClient;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
public class TestController {


    @Autowired
    private WebClient webClient;

    @GetMapping("sayHello")
    public Mono<String> sayHello() {

        DirectedAcyclicGraph<TaskNode, DefaultEdge> dag = new DirectedAcyclicGraph<>(DefaultEdge.class);
        TaskNode node1 = new TaskNode("node1", HttpMethod.GET, 30080, "localhost", "xxx/health");
        TaskNode node2 = new TaskNode("node2", HttpMethod.GET, 30080, "localhost", "xxx/health");
        TaskNode node3 = new TaskNode("node3", HttpMethod.GET, 30080, "localhost", "xxx/health");
        TaskNode node4 = new TaskNode("node4", HttpMethod.GET, 30080, "localhost", "xxx/health");
        TaskNode node5 = new TaskNode("node5", HttpMethod.GET, 30080, "localhost", "xxx/health");
        dag.addVertex(node1);
        dag.addVertex(node2);
        dag.addVertex(node3);
        dag.addVertex(node4);
        dag.addVertex(node5);
        dag.addEdge(node1, node2);
        dag.addEdge(node1, node3);
        dag.addEdge(node2, node4);
        dag.addEdge(node3, node4);
        dag.addEdge(node5, node2);

        HashMap<String, Object> parameterMap = new HashMap<>();

        ConcurrentHashMap<TaskNode, Future> futureMap = new ConcurrentHashMap<>();
        TopologicalOrderIterator<TaskNode, DefaultEdge> iterator = new TopologicalOrderIterator<>(dag);
        while (iterator.hasNext()) {
            TaskNode node = iterator.next();
            Set<DefaultEdge> incomingEdges = dag.incomingEdgesOf(node);
            List<Future> futures = incomingEdges.stream()
                    .map(edge -> dag.getEdgeSource(edge))
                    .map(sourceNode -> futureMap.get(sourceNode))
                    .collect(Collectors.toList());

            Future<Void> future = CompositeFuture.all(futures)
                    .compose(compositeResult -> {
                        if (compositeResult.succeeded()) {
                            System.out.println(node.name + "---" + parameterMap);
                        }
                        return node.execute(webClient, parameterMap);
                    });
            futureMap.put(node, future);
        }

        return Mono.create(sink -> {
            CompositeFuture.all(futureMap.values().stream().collect(Collectors.toList()))
                    .onComplete(compositeResult -> {
                                if (compositeResult.succeeded()) {
                                    System.out.println(parameterMap);
                                    sink.success("hello");
                                } else {
                                    sink.error(compositeResult.cause());
                                }
                            }
                    );
        });
    }


    public static class TaskNode {
        private final String name;
        private final HttpMethod method;
        private final Integer port;
        private final String host;
        private final String uri;

        public TaskNode(String name, HttpMethod method, Integer port, String host, String uri) {
            this.name = name;
            this.method = method;
            this.port = port;
            this.host = host;
            this.uri = uri;
        }

        public Future<Void> execute(WebClient webClient, Map<String, Object> parameterMap) {
            Promise<Void> promise = Promise.promise();
            webClient.request(method, port, host, uri)
                    .putHeader("user_id", "xxx")
                    .send()
                    .onSuccess(response -> {
                        if (response.statusCode() == 200) {
                            parameterMap.put(name, response.bodyAsString());
                            promise.complete();
                        }
                    })
                    .onFailure(throwable -> {
                        promise.fail(throwable);
                    });
            return promise.future();
        }

    }
}


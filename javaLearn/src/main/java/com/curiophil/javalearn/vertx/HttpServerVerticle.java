package com.curiophil.javalearn.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HttpServerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/").handler(this::handleRoot);
        router.get("/hello").handler(this::handleHello);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, result -> {
                    if (result.succeeded()) {
                        System.out.println("HTTP server started on port 8888");
                    } else {
                        System.out.println("HTTP server failed to start: " + result.cause());
                    }
                });
    }

    private void handleRoot(RoutingContext context) {
        context.response()
                .putHeader("content-type", "text/plain")
                .end("Welcome to Vert.x HTTP Server!");
    }

    private void handleHello(RoutingContext context) {
        context.response()
                .putHeader("content-type", "text/plain")
                .end("Hello, Vert.x!");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());
        System.out.println("-------------------------------------------------------------------------");
    }
}


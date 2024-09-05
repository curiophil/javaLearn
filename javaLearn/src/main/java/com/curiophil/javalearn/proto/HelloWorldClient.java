package com.curiophil.javalearn.proto;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder()
                .setName("World")
                .build();

        HelloWorldProto.HelloReply response = stub.sayHello(request);

        System.out.println("Client received: " + response.getMessage());

        channel.shutdown();
    }
}

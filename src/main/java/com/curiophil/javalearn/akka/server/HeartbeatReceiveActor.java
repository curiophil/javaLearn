package com.curiophil.javalearn.akka.server;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class HeartbeatReceiveActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        Receive receive = receiveBuilder().match(String.class, s -> {
            System.out.println(String.format("Receive %s's heartbeat. Message is %s.", sender(), s));
            sender().tell("success", ActorRef.noSender());
        }).build();
        return receive;
    }
}

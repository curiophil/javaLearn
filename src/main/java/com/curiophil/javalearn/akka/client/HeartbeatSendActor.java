package com.curiophil.javalearn.akka.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class HeartbeatSendActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        Receive receive = receiveBuilder().match(String.class, s -> {
            System.out.println(s);
        }).build();
        return receive;
    }
}

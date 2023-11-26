package com.curiophil.javalearn.akka.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ServerMain {

    public static void main(String[] args) {
        Config config = ConfigFactory.load("akka-server.conf");
        ActorSystem actorSystem = ActorSystem.create("server", config);
        actorSystem.actorOf(Props.create(HeartbeatReceiveActor.class), "heartbeatReceiveActor");
    }
}

package com.curiophil.javalearn.akka.client;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ClientMain {

    public static void main(String[] args) {
        Config config = ConfigFactory.load("akka-client.conf");
        ActorSystem actorSystem = ActorSystem.create("client", config);
        ActorSelection actorSelection = actorSystem.actorSelection("akka://server@127.0.0.1:25555/user/heartbeatReceiveActor");
        ActorRef sender = actorSystem.actorOf(Props.create(HeartbeatSendActor.class));
        while (true) {
            actorSelection.tell("sender-" + sender.hashCode(), sender);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.cuiriophil.webflux;


import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class WebfluxServiceApplication {

    @Bean
    public HazelcastClusterManager clusterManager() {
        // 创建 Hazelcast 配置对象
        Config hazelcastConfig = new Config();

        // 设置集群网络配置
        NetworkConfig networkConfig = hazelcastConfig.getNetworkConfig();

        // 设置本地端口 (你可以为每个实例使用不同的端口)
        networkConfig.setPort(5701).setPortAutoIncrement(true);  // 端口自增

        // 配置本地 TCP/IP 集群加入策略
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);  // 禁用组播
        joinConfig.getTcpIpConfig().setEnabled(true)
                .addMember("127.0.0.1");  // 使用本地主机作为集群成员

        // 创建 HazelcastClusterManager 并传入配置
        HazelcastClusterManager clusterManager = new HazelcastClusterManager(hazelcastConfig);
        return clusterManager;
    }

    @Bean
    public Vertx vertx(HazelcastClusterManager clusterManager) throws ExecutionException, InterruptedException {
        // 设置 Vert.x 集群选项
        VertxOptions options = new VertxOptions().setWorkerPoolSize(16).setClusterManager(clusterManager);

        Promise<Vertx> promise = Promise.promise();

        Vertx.clusteredVertx(options, promise);

        // 等待完成，获取结果
        Future<Vertx> future = promise.future();
        Vertx vertx = future.toCompletionStage().toCompletableFuture().get();
        if (vertx != null) {
            System.out.println("Vert.x 集群节点启动成功！");

            HazelcastInstance hazelcastInstance = clusterManager.getHazelcastInstance();
            if (hazelcastInstance != null) {
                hazelcastInstance.getCluster().addMembershipListener(new MembershipListener() {
                    @Override
                    public void memberAdded(MembershipEvent membershipEvent) {
                        System.out.println("节点加入集群: " + membershipEvent.getMember().getAddress());
                    }

                    @Override
                    public void memberRemoved(MembershipEvent membershipEvent) {
                        System.out.println("节点离开集群: " + membershipEvent.getMember().getAddress());
                        // 这里可以执行节点离开后的相应操作
                    }
                });
            } else {
                System.out.println("Hazelcast 实例未初始化");
            }
        } else {
            System.out.println("Vert.x 集群节点启动失败");
        }
        return vertx;
    }

    @Bean
    public WebClient webClient(Vertx vertx) {
        return WebClient.create(vertx);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebfluxServiceApplication.class, args);
    }

}

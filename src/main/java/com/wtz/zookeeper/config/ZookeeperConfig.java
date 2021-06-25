package com.wtz.zookeeper.config;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper配置
 *
 * @author wangtianzeng
 */
@Configuration
public class ZookeeperConfig {

    @Resource
    private ZookeeperProperties zookeeperProperties;

    @Bean
    public ZooKeeper zooKeeper(){
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(zookeeperProperties.getAddress(), zookeeperProperties.getSessionTimeoutMs(), watchedEvent -> {
                if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
        } catch (Exception e){
            e.printStackTrace();
        }
        return zooKeeper;
    }
}

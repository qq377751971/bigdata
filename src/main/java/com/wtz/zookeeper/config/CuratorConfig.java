package com.wtz.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * curator 配置
 *
 * @author wangtianzeng
 */
@Configuration
public class CuratorConfig {

    @Resource
    private ZookeeperProperties zookeeperProperties;

    @Bean
    public CuratorFramework curatorFramework(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getAddress())
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
                .retryPolicy(new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(), zookeeperProperties.getMaxRetries()))
                .namespace(zookeeperProperties.getNamespace())
                .authorization("digest", zookeeperProperties.getDigest().getBytes(StandardCharsets.UTF_8))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }
}

package com.wtz.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zookeeper配置项
 *
 * @author wangtianzeng
 */
@Data
@Component
@ConfigurationProperties(prefix = "zookeeper")
public class ZookeeperProperties {

    private boolean enabled;

    private String address;

    private String namespace;

    private String digest;

    private int sessionTimeoutMs;

    private int connectionTimeoutMs;

    private int maxRetries;

    private int baseSleepTimeMs;
}

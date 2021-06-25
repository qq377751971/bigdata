package com.wtz.zookeeper.curator;

import com.wtz.utils.YamlUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * apache curator test
 */
public class CuratorTest {

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(YamlUtil.getProperty("zookeeper", "address").toString())
                .sessionTimeoutMs(Integer.parseInt(YamlUtil.getProperty("zookeeper", "sessionTimeoutMs").toString()))
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("")
                .build();
        curatorFramework.start();
        ExistsBuilder existsBuilder = curatorFramework.checkExists();
        try {
            Stat stat = existsBuilder.forPath("/zookeeper");
            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

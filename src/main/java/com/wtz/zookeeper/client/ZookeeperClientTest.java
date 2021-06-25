package com.wtz.zookeeper.client;

import com.wtz.utils.YamlUtil;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * zookeeper原生客户端
 *
 * @author wangtianzeng
 */
public class ZookeeperClientTest {

    public static void main(String[] args) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper =
                    new ZooKeeper(YamlUtil.getProperty("zookeeper", "address").toString(),
                            Integer.parseInt(YamlUtil.getProperty("zookeeper", "sessionTimeoutMs").toString()), event -> {
                        if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                            //如果收到了服务端的响应事件，连接成功
                            countDownLatch.countDown();
                        }
                    });
            countDownLatch.await();
            //CONNECTED
            System.out.println(zooKeeper.getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

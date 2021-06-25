package com.wtz.zookeeper.client;

import com.wtz.utils.YamlUtil;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * zookeeper 101tec client
 *
 * @author wangtianzeng
 */
public class Zookeeper101TecTest {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(YamlUtil.getProperty("zookeeper.address").toString());
        List<String> children = zkClient.getChildren("/zookeeper");
        System.out.println(children);
    }
}

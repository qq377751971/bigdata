package com.wtz.zookeeper.config;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * zookeeper api
 *
 * @author wangtianzeng
 */
@Component
public class ZookeeperApi {

    @Resource
    private ZooKeeper zooKeeper;

    public Stat exists(String path, boolean needWatch){
        try {
            return zooKeeper.exists(path, needWatch);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Stat exists(String path, Watcher watcher){
        try {
            return zooKeeper.exists(path, watcher);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean createNode(String path, String data){
        try {
            zooKeeper.create(path, data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateNode(String path, String data){
        try {
            zooKeeper.setData(path, data.getBytes(StandardCharsets.UTF_8), -1);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteNode(String path){
        try {
            zooKeeper.delete(path, -1);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getChildren(String path){
        try {
            return zooKeeper.getChildren(path, false);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getData(String path, Watcher watcher){
        try {
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(path, watcher, stat);
            return new String(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // @PostConstruct
    public void run(){
        String path = "/zk-test-1";
        System.out.println("[执行测试方法1]");
        this.createNode(path, "测试1");
        String value = this.getData(path, new ZookeeperWatcherApi());
        System.out.println("[执行测试方法1返回值]：" + value);
        this.deleteNode(path);
    }
}

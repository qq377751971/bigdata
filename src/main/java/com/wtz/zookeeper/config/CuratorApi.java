package com.wtz.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * apache curator api
 *
 * @author wangtianzeng
 */
@Component
public class CuratorApi {

    @Resource
    private CuratorFramework curatorFramework;

    public Stat exists(String path){
        try {
            return curatorFramework.checkExists().forPath(path);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean createNode(String path, String data){
        try {
            // 递归创建
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateNode(String path, String data){
        try {
            curatorFramework.setData().forPath(path, data.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteNode(String path){
        try {
            // 递归删除
            curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getChildren(String path){
        try {
            return curatorFramework.getChildren().forPath(path);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getData(String path, Watcher watcher){
        try {
            Stat stat = new Stat();
            byte[] data = curatorFramework.getData().usingWatcher(watcher).forPath(path);
            return new String(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    @PostConstruct
    public void run(){
        String path = "/zk-test-2";
        System.out.println("[执行测试方法2]");
        this.createNode(path, "测试2");
        String value = this.getData(path, new ZookeeperWatcherApi());
        System.out.println("[执行测试方法2返回值]：" + value);
        this.deleteNode(path);
    }
}

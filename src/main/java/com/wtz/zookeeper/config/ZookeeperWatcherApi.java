package com.wtz.zookeeper.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * zookeeper watcher api
 *
 * @author wangtianzeng
 */
public class ZookeeperWatcherApi implements Watcher {

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("[Watcher event]" + watchedEvent.getState());
        System.out.println("[Watcher path]" + watchedEvent.getPath());
        System.out.println("[Watcher type]" + watchedEvent.getType());
    }
}

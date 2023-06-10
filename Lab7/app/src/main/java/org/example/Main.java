package org.example;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 15000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                LOG.info("*********************************************************");
                LOG.info("got the event for node = "+ watchedEvent.getPath());
                LOG.info("the event type = "+ watchedEvent.getType());
                LOG.info("*********************************************************");
            }
        });

        // CREATE
        zookeeper.create("/node", "data".getBytes(), OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, null);

        // READ
        Stat stat = new Stat();
        var data = zookeeper.getData("/node", true, stat);

        Thread.sleep(100_000);
    }
}
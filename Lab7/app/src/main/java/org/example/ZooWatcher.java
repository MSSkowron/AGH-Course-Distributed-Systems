package org.example;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ZooWatcher implements Watcher {
    private static final Logger LOG = LoggerFactory.getLogger(ZooWatcher.class);

    private static final String NODE_PATH = "/z";

    private final ZooKeeper zooKeeper;
    private final String[] executableArgs;
    private Process executable = null;
    private int descendantCounter = 0;

    public ZooWatcher(ZooKeeper zooKeeper, String[] executableArgs) throws KeeperException, InterruptedException {
        this.zooKeeper = zooKeeper;
        this.executableArgs = executableArgs;
        initialize();
    }

    private void initialize() throws KeeperException, InterruptedException {
        if (zooKeeper.exists(NODE_PATH, this) == null) {
            waitForNode();
        } else {
            waitForNode();
            countDescendants(NODE_PATH);
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            LOG.error("Required host and application path");
            return;
        }

        String host = args[0];
        String[] executableArgs = Arrays.copyOfRange(args, 1, args.length);

        try {
            ZooKeeper zooKeeper = new ZooKeeper(host, 3000, event -> {});
            ZooWatcher zooWatcher = new ZooWatcher(zooKeeper, executableArgs);
            zooWatcher.runCommandLoop();
        } catch (Exception e) {
            LOG.error("An error occurred while running ZooWatcher: ", e);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        assert event.getPath().equals(NODE_PATH);

        try {
            switch (event.getType()) {
                case NodeDeleted:
                    System.out.println("Deleted node");
                    terminate();
                    waitForNode();
                    descendantCounter = 0;
                    break;
                case NodeChildrenChanged:
                    System.out.println("Node children changed");
                    countDescendants(NODE_PATH);
                    break;
                case NodeCreated:
                    System.out.println("Created node");
                    waitForNode();
                    run();
                    countDescendants(NODE_PATH);
                    break;
            }
        } catch (KeeperException e) {
            terminate();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOG.error("An error occurred", e);
        }
    }

    private void waitForNode() throws KeeperException, InterruptedException {
        zooKeeper.exists(NODE_PATH, this);
        if (zooKeeper.exists(NODE_PATH, false) != null) {
            watchDescendants(NODE_PATH);
        }
    }

    private void watchDescendants(String path) throws KeeperException, InterruptedException {
        for (String child : zooKeeper.getChildren(path, this)) {
            String childPath = path + "/" + child;
            watchDescendants(childPath);
        }
    }

    private void countDescendants(String path) throws KeeperException, InterruptedException {
        try {
            for (String child : zooKeeper.getChildren(path, false)) {
                countDescendants(path + "/" + child);
            }

            if (path.equals(NODE_PATH)) {
                if (descendantCounter == 1) {
                    System.out.printf(("Path %s has 1 descendant%n"), NODE_PATH);
                } else {
                    System.out.printf("Path %s has %d descendants%n", NODE_PATH, descendantCounter);
                }
                descendantCounter = 0;
            } else {
                descendantCounter++;
            }
        } catch (KeeperException e) {
            LOG.error("Keeper exception during counting children", e);
            terminate();
            waitForNode();
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception during counting children");
            Thread.currentThread().interrupt();
        }
    }

    private void terminate() {
        if (isExecutableEligibleForTermination()) {
            executable.destroy();
        }
    }

    private boolean isExecutableEligibleForTermination() {
        return executable != null && executable.isAlive();
    }

    private void run() {
        try {
            if (isExecutableEligibleForRun()) {
                executable = Runtime.getRuntime().exec(executableArgs);
            }
        } catch (IOException e) {
            LOG.error("Fail during launching application", e);
        }
    }

    private boolean isExecutableEligibleForRun() {
        return executable == null || !executable.isAlive();
    }

    private void runCommandLoop() {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = inputReader.readLine().trim();
                switch (input) {
                    case "printTree":
                        printTree();
                        break;
                    case "quit":
                        System.exit(0);
                        break;
                }
            } catch (IOException e) {
                LOG.error("An error occurred while reading input", e);
            } catch (Exception e) {
                LOG.error("An error occurred", e);
            }
        }
    }

    private void printTree() {
        try {
            if (zooKeeper.exists(NODE_PATH, false) != null) {
                System.out.println("Znode path: " + NODE_PATH);
                printChildren(NODE_PATH);
            } else {
                LOG.error("Znode: " + NODE_PATH + " does not exist");
            }
        } catch (Exception e) {
            LOG.error("An error occurred", e);
        }
    }

    private void printChildren(String path) throws KeeperException, InterruptedException {
        for (String child : zooKeeper.getChildren(path, false)) {
            String childPath = path + "/" + child;
            System.out.println(childPath);
            printChildren(childPath);
        }
    }
}

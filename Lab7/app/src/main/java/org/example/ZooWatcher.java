package org.example;

import org.apache.zookeeper.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ZooWatcher implements Watcher {
    private static final String NODE_PATH = "/z";
    private final ZooKeeper zooKeeper;
    private final String[] executableArgs;
    private Process executable = null;
    private int descendantCounter = 0;

    public ZooWatcher(String host, String[] appArgs) throws IOException, KeeperException, InterruptedException {
        zooKeeper = new ZooKeeper(host, 3000, (ignore) -> {});
        this.executableArgs = appArgs;
        if (zooKeeper.exists(NODE_PATH, this) == null) {
            waitForNode();
        } else {
            waitForNode();
            countDescendants(NODE_PATH);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        if (args.length < 2) {
            System.out.println("Required host and application path");
        } else {
            String host = args[0];
            String[] executableArgs = Arrays.copyOfRange(args, 1, args.length);
            ZooWatcher zooWatcher = new ZooWatcher(host, executableArgs);
            zooWatcher.run();
        }
    }

    public void run() throws IOException, KeeperException, InterruptedException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = inputReader.readLine().trim();
            switch (input) {
                case "printTree":
                    printTree(NODE_PATH);
                    break;
                case "quit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    public void process(WatchedEvent event) {
        assert event.getPath().equals(NODE_PATH);
        try {
            switch (event.getType()) {
                case NodeDeleted:
                    System.out.println("Deleted node ");
                    terminateExecutable();
                    waitForNode();
                    descendantCounter = 0;
                    break;
                case NodeChildrenChanged:
                    System.out.println("Node children changed ");
                    countDescendants(NODE_PATH);
                    waitForNode();
                    break;
                case NodeCreated:
                    System.out.println("Created node ");
                    waitForNode();
                    runExecutable();
                    countDescendants(NODE_PATH);
                    break;
            }
        } catch (KeeperException e) {
            terminateExecutable();
        } catch (InterruptedException e) {
            System.out.println("Interrupted ");
        }
    }

    private void waitForNode() throws KeeperException, InterruptedException {
        zooKeeper.exists(NODE_PATH, this, null, null);
        if (zooKeeper.exists(NODE_PATH, false) != null)
            watchDescendants(NODE_PATH);
    }

    private void watchDescendants(String path) throws KeeperException, InterruptedException {
        zooKeeper.getChildren(path, this);
        for (String child : zooKeeper.getChildren(path, this)) {
            watchDescendants(path + "/" + child);
        }
    }

    private void countDescendants(String path) throws KeeperException, InterruptedException {
        try {
            for (String child : zooKeeper.getChildren(path, false)) {
                countDescendants(path + "/" + child);
            }
            if (path.equals(NODE_PATH)) {
                if (descendantCounter == 1) {
                    System.out.println(String.format(("Path %s has 1 descendant"), NODE_PATH));
                } else {
                    System.out.println(String.format("Path %s has %d descendants", NODE_PATH, descendantCounter));
                }
                descendantCounter = 0;
            } else {
                descendantCounter++;
            }
        } catch (KeeperException e) {
            System.out.println("Keeper exception during counting children");
            terminateExecutable();
            waitForNode();
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception during counting children");
        }
    }

    private void terminateExecutable() {
        if (isExecutableRunning()) {
            executable.destroy();
        }
    }

    private boolean isExecutableRunning() {
        return executable != null && executable.isAlive();
    }

    private void runExecutable() {
        try {
            if (!isExecutableRunning()) {
                executable = Runtime.getRuntime().exec(executableArgs);
            }
        } catch (IOException e) {
            System.out.println("Failed to launch the application");
        }
    }

    private void printTree(String path) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(path, false) != null) {
            System.out.println("Znode path: " + path);
            printChildrenRecursively(path);
        } else {
            System.out.println("Znode: " + path + " does not exist");
        }
    }

    private void printChildrenRecursively(String path) throws KeeperException, InterruptedException {
        for (String child : zooKeeper.getChildren(path, false)) {
            String childPath = path + "/" + child;
            System.out.println("Child path: " + childPath);
            printChildrenRecursively(childPath);
        }
    }
}

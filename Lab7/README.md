# ZooWatcher App

The ZooWatcher app is a Java application that monitors a ZooKeeper node and performs various actions based on events occurring in the node.

## Usage

1. Clone the repository or download the source code files.

2. Build the application using a Java compiler or an integrated development environment (IDE) of your choice.

3. Make sure you have a running ZooKeeper servers.

   You can run it using the provided bat scripts. Run the following command in the `Lab7` directory:

   ```
   ./zookeeper/apache-zookeeper-3.8.0-bin/startServers.bat
   ```

4. Run the ZooWatcher class with the following arguments:

   - `ZooKeeperServerAddress` - The address and port of your ZooKeeper server
   - `ApplicationPath` - Path to the application you want to execute when the specified ZooKeeper node events occur.

   ```
    java ZooWatcher <ZooKeeperServerAddress> <ApplicationPath>
   ```

5. Once the `ZooWatcher` app is running, you can interact with it through the command line.
   The following commands are available:

   - `printTree` - Prints the tree structure of the ZooKeeper node being monitored.
   - `quit` - Terminates the application.

## Functionality

The ZooWatcher app monitors a specified ZooKeeper node and performs the following actions:

- `Node Creation` - When the monitored node is created, the app waits for the node to become available, runs the specified application, and counts the number of descendants under the node.
- `Node Deletion` - If the monitored node is deleted, the app terminates the running application (if any), waits for the node to be created again, and resets the descendant counter.
- `Node Children Changed` - When the children of the monitored node change, the app counts the number of descendants under the node and waits for the node to become available.

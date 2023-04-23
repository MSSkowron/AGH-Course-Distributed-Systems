# Local cluster

1.  **Dependencies**

    Before running the code, you need to ensure that you have the necessary dependencies installed. These include:

    - Python
    - Ray
    - Make

2.  **Start Ray cluster**

    You need to start a Ray cluster to run the code. To start the Ray cluster in head mode, run the following command inside _local-cluster_ directory:

    ```
    make run-head
    ```

    This will start the Ray head node with the Ray dashboard enabled.

3.  **Start Ray workers**

    After starting the Ray cluster in head mode, you need to start Ray workers. Run the following command inside _local-cluster_ directory to start Ray workers:

    ```
    make run-worker ADDRESS=<head-node-ip-address>
    ```

    Replace <head-node-ip-address> with the IP address of the machine running the Ray head node. You can start as many workers as needed.

4.  **Run tasks**

    There are several tasks that can be run, including tasks involving actors and objects. To run a task, use one of the following commands inside _local-cluster_ directory:

    ```
    make run-task
    make run-objects
    make run-actors1
    make run-actors2
    make run-actors3
    ```

# Dockerized cluster

1. **Dependencies**

   Before running the code, you need to ensure that you have the necessary dependencies installed. These include:

   - Python
   - Docker

2. **Run docker container**

   Run the following command inside _docker-cluster_ directory

   ```
   docker-compose up
   ```

3. **Run tasks**

   There are several tasks that can be run, including tasks involving actors and objects. To run a task, use one of the following commands inside _docker-cluster_ directory:

   ```
   python ray-lab-1-task.py
   python ray-lab-2-objects.py
   python ray-lab-3-actors-1.py
   python ray-lab-3-actors-2.py
   python ray-lab-3-actors-3.py
   ```

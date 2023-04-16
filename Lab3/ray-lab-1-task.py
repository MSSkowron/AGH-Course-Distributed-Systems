import os
import logging
import time
import ray
import cProfile

# Excercises 1.1)Try using local bubble sort and remote bubble sort, show difference
if ray.is_initialized:
    ray.shutdown()
ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)

ARRAY_SIZE = 1000
NUM_OF_ARRAYS = 100

@ray.remote
def remote_bubble_sort(array):
    n = len(array)
    for i in range(n):
        for j in range(n - i - 1):
            if array[j] > array[j + 1]:
                array[j], array[j + 1] = array[j + 1], array[j]
    return array


def local_bubble_sort(array):
    n = len(array)
    for i in range(n):
        for j in range(n - i - 1):
            if array[j] > array[j + 1]:
                array[j], array[j + 1] = array[j + 1], array[j]
    return array


# Local function
def run_local():
    results = [local_bubble_sort([i for i in range(ARRAY_SIZE, 0, -1)]) for _ in range(NUM_OF_ARRAYS)]
    return results


# Distributed on a Ray cluster
def run_remote():
    results = ray.get([remote_bubble_sort.remote([i for i in range(ARRAY_SIZE, 0, -1)]) for _ in range(NUM_OF_ARRAYS)])
    return results


print('local run')
cProfile.run("run_local()")

print('remote run')
cProfile.run("run_remote()")

ray.shutdown()

import os
import logging
import time
import ray
import cProfile

# Excercises 1.1)Try using local bubble sort and remote bubble sort, show difference
if ray.is_initialized:
    ray.shutdown()
ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)

arr = [7, 3, 5, 1, 9, 2, 8, 4, 6, 0]


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
    start_time = time.time()
    results = [local_bubble_sort(arr) for _ in range(os.cpu_count())]
    print(f"LOCAL --- {time.time() - start_time:.2f} seconds ---")
    print(results)

    return results


# Distributed on a Ray cluster
def run_remote():
    start_time = time.time()
    results = ray.get([remote_bubble_sort.remote(arr) for _ in range(os.cpu_count())])
    print(f"REMOTE --- {time.time() - start_time:.2f} seconds ---")
    print(results)

    return results


print('local run')
cProfile.run("run_local()")

print('remote run')
cProfile.run("run_remote()")

ray.shutdown()

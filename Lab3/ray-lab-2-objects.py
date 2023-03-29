import logging
import ray

# Excercises 2.1) Create large lists and python dictionaries,
# put them in object store. Write a Ray task to process them.
if ray.is_initialized:
    ray.shutdown()
ray.init(logging_level=logging.ERROR)

@ray.remote
def process_data(data):
    # Process data here
    even_sum = sum(num for num in data if num % 2 == 0)
    return even_sum

large_list = [i for i in range(1000000)]
large_list_id = ray.put(large_list)

large_dict = {i: i**2 for i in range(1000000)}
large_dict_id = ray.put(large_dict)

list_result_id = process_data.remote(large_list_id)
dict_result_id = process_data.remote(large_dict_id)

list_result = ray.get(list_result_id)
dict_result = ray.get(dict_result_id)

print("List result: ", list_result)
print("Dictionary result: ", dict_result)

ray.shutdown()
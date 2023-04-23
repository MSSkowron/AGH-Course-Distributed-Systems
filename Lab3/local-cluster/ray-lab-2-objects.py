import logging
import ray

# Excercises 2.1) Create large lists and python dictionaries,
# put them in object store. Write a Ray task to process them.
if ray.is_initialized:
    ray.shutdown()
ray.init(address='auto')


@ray.remote
def process_data(data):
    # Process data here
    even_sum = sum(num for num in data if num % 2 == 0)
    return even_sum


# Create many large lists and dictionaries
num_lists = 10
num_dicts = 10

large_lists = [[i for i in range(1000000)] for j in range(num_lists)]
large_dicts = [{i: i * 2 for i in range(1000000)} for j in range(num_dicts)]

# Put the lists and dictionaries in the Ray object store
large_list_refs = [ray.put(large_list) for large_list in large_lists]
large_dict_refs = [ray.put(large_dict) for large_dict in large_dicts]

# Process the large lists and dictionaries asynchronously using remote functions
large_list_results_refs = [process_data.remote(large_list_ref) for large_list_ref in large_list_refs]
large_dict_results_refs = [process_data.remote(large_dict_ref) for large_dict_ref in large_dict_refs]

# Get results
large_list_results = ray.get(large_list_results_refs)
large_dict_results = ray.get(large_dict_results_refs)

# Print results
print("List results: ", large_list_results)
print("Dictionary results: ", large_dict_results)

ray.shutdown()

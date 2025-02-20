import time
import ray
import random

# 3.2 Modify method invoke to return a random int value between [5, 25]
if ray.is_initialized:
    ray.shutdown()
ray.init(address="ray://localhost:10001")

CALLERS = ["A", "B", "C"]


@ray.remote
class MethodStateCounter:
    def __init__(self):
        self.invokers = {"A": 0, "B": 0, "C": 0}
        self.invokers_computations = {"A": list(), "B": list(), "C": list()}

    def invoke(self, name):
        # pretend to do some work here
        time.sleep(0.5)
        value = random.randint(5, 25)
        self.invokers_computations[name].append(value)
        # update times invoked
        self.invokers[name] += 1
        # return the state of that invoker
        return value

    def get_invoker_state(self, name):
        # return the state of the named invoker
        return self.invokers[name]

    def get_all_invoker_state(self):
        # reeturn the state of all invokers
        return self.invokers

    def get_invoker_computations(self, name):
        return self.invokers_computations[name]

    def get_all_invoker_computations(self):
        return self.invokers_computations


worker_invoker = MethodStateCounter.remote()
print(worker_invoker)

for _ in range(10):
    name = random.choice(CALLERS)
    worker_invoker.invoke.remote(name)

print('method callers')
for _ in range(5):
    random_name_invoker = random.choice(CALLERS)
    value_computed = ray.get(worker_invoker.invoke.remote(random_name_invoker))
    print(f"Named caller: {random_name_invoker} computed value {value_computed}")

print('invoker call count')
for caller in CALLERS:
    call_count = ray.get(worker_invoker.get_invoker_state.remote(caller))
    print(f"{caller} was called {call_count} times")

print('invoker values')
for caller in CALLERS:
    values = ray.get(worker_invoker.get_invoker_computations.remote(caller))
    print(f"{caller} computed values: {values}")

print(ray.get(worker_invoker.get_all_invoker_state.remote()))

ray.shutdown()
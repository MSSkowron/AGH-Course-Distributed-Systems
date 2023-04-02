import logging
import math
import time
import ray
import random
from fractions import Fraction

# 3.3 Take a look on implement parralel Pi computation
# based on https://docs.ray.io/en/master/ray-core/examples/highly_parallel.html
#
# Implement calculating pi as a combination of actor (which keeps the
# state of the progress of calculating pi as it approaches its final value)
# and a task (which computes candidates for pi)
if ray.is_initialized:
    ray.shutdown()
ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)

# Number of samples
SAMPLE_COUNT = 1000 * 1000
# Number of all samples
FULL_SAMPLE_COUNT = 1000 * 1000 * 1000
# Number of batches (iterations)
BATCHES = int(FULL_SAMPLE_COUNT / SAMPLE_COUNT)

# PiActor class as actor which stores results of computations
@ray.remote
class PiActor:
    def __init__(self):
        self.results = []

    def add_value(self, value: int):
        self.results.append(value)

    def get_current_value(self):
        try:
            return sum(self.results) * 4 / len(self.results)
        except ZeroDivisionError:
            return 0


@ray.remote
def pi4_sample(pi_actor: PiActor, sample_count: int):
    in_count = 0
    for i in range(sample_count):
        x = random.random()
        y = random.random()
        if x * x + y * y <= 1:
            in_count += 1

    fraction = Fraction(in_count, sample_count)
    pi_actor.add_value.remote(fraction)
    return fraction


piActor = PiActor.remote()
pi4_refs = [pi4_sample.remote(piActor, SAMPLE_COUNT) for _ in range(BATCHES)]

print(f'Doing {BATCHES} batches')
start = time.time()

ready, remaining = ray.wait(pi4_refs, num_returns=len(pi4_refs), timeout=1)
while len(remaining) > 0:
    print(f'PROGRESS: {round((len(ready)/BATCHES)*100, 2)}%')

    ready, remaining = ray.wait(pi4_refs, num_returns=len(pi4_refs), timeout=1)
    time.sleep(1)

end = time.time()
dur = end - start

print(f'Took {round(dur, 2)} seconds')

pi = float(ray.get(piActor.get_current_value.remote()))
print(f"Estimated value of π is: {float(pi)}")
print(f"Inaccuracy: {abs(pi-math.pi)/pi}")

ray.shutdown()

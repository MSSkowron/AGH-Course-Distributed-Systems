PYTHON = python
TASK = ray-lab-1-task.py
OBJECTS = ray-lab-2-objects.py
ACTORS1 = ray-lab-3-actors-1.py
ACTORS2 = ray-lab-3-actors-2.py
ACTORS3 = ray-lab-3-actors-3.py

run-head:
	ray start --head --port=6379 --include-dashboard=true

run-worker:
	ray start --address=$(ADDRESS):6379

run-task:
	$(PYTHON) $(TASK)

run-objects:
	$(PYTHON) $(OBJECTS)

run-actors1:
	$(PYTHON) $(ACTORS1)

run-actors2:
	$(PYTHON) $(ACTORS2)

run-actors3:
	$(PYTHON) $(ACTORS3)
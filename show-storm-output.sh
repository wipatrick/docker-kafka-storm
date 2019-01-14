#!/bin/bash

docker exec -it docker-kafka-storm_supervisor_1 tail -f /var/log/storm/worker-6702.log

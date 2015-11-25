#!/bin/bash

docker exec -it dockerkafkastorm_supervisor_1 tail -f /var/log/storm/worker-6702.log

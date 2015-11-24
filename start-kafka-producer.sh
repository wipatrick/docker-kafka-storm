#!/bin/bash

docker run -it --rm \
        -e BROKER_HOST=$1 \
        -e BROKER_PORT=$2 \
        -e TOPIC=$3 \
        -e COUNTER_END=$4 \
        -e SLEEP_TIME_IN_MILLIS=$5 \
        --name producer \
        kafka-producer

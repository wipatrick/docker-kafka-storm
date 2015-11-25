#!/bin/bash

BROKER_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' dockerkafkastorm_kafka_1)
# Only works for container ports, that are mapped/exposed on the Host
BROKER_PORT=$(docker inspect --format '{{ (index (index .NetworkSettings.Ports "9092/tcp") 0).HostPort }}' dockerkafkastorm_kafka_1)

docker run -it --rm \
        -e BROKER_HOST=$BROKER_HOST \
        -e BROKER_PORT=$BROKER_PORT \
        -e TOPIC=$1 \
        -e COUNTER_END=$2 \
        -e SLEEP_TIME_IN_MILLIS=$3 \
        --name producer \
        kafka-producer

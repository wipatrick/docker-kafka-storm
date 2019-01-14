#!/bin/bash

ZK_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' docker-kafka-storm_zookeeper_1)
# Only works for container ports, that are mapped/exposed on the Host
ZK_PORT=$(docker inspect --format '{{ (index (index .NetworkSettings.Ports "2181/tcp") 0).HostPort }}' docker-kafka-storm_zookeeper_1)

docker run -it --rm \
        --name=kafka-console-consumer \
        ches/kafka /bin/bash -c "/kafka/bin/kafka-console-consumer.sh --zookeeper $ZK_HOST:$ZK_PORT --topic $1"

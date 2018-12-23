#!/bin/bash

ZK_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' docker-kafka-storm_zookeeper_1)
# Only works for container ports, that are mapped/exposed on the Host
ZK_PORT=$(docker inspect --format '{{ (index (index .NetworkSettings.Ports "2181/tcp") 0).HostPort }}' docker-kafka-storm_zookeeper_1)

# create topic
docker run -it --rm \
        --name create-kafka-topic \
        ches/kafka \
        /bin/bash -c "/kafka/bin/kafka-topics.sh --create --zookeeper $ZK_HOST:$ZK_PORT --replication-factor $1 --partition $2 --topic $3"

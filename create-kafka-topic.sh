#!/bin/bash

# create topic
docker run -it --rm \
        --name create-kafka-topic \
        ches/kafka \
        /bin/bash -c "/kafka/bin/kafka-topics.sh --create --zookeeper $1:$2 --replication-factor $3 --partition $4 --topic $5"

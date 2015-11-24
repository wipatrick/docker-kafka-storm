#!/bin/bash

docker run -it --rm \
        -e MAINCLASS=$1 \
        -e TOPOLOGY_NAME=$2 \
        -e ZK_HOST=$3 \
        -e ZK_PORT=$4 \
        -e TOPIC=$5 \
        -e NIMBUS_HOST=$6 \
        -e NIMBUS_THRIFT_PORT=$7 \
        --name topology \
        storm-topology \
        "submit"

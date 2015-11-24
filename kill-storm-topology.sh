#!/bin/bash

docker run -it --rm \
        -e TOPOLOGY_NAME=$1 \
        -e NIMBUS_HOST=$2 \
        -e NIMBUS_THRIFT_PORT=$3 \
        --name topology \
        storm-topology \
        "kill"

#!/bin/bash

# NIMBUS_HOST="$( echo $(docker exec -it dockerkafkastorm_supervisor_1 /bin/bash -c "env | grep NIMBUS_PORT_6627_TCP_ADDR") | awk -F'=' '{print $NF}')"
# NIMBUS_THRIFT_PORT="$( echo $(docker exec -it dockerkafkastorm_supervisor_1 /bin/bash -c "env | grep NIMBUS_PORT_6627_TCP_PORT") | awk -F'=' '{print $NF}' | sed 's/[^0-9]//g')"

NIMBUS_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' dockerkafkastorm_nimbus_1)
# Only works for container ports, that are mapped/exposed on the Host
NIMBUS_THRIFT_PORT=$(docker inspect --format '{{ (index (index .NetworkSettings.Ports "6627/tcp") 0).HostPort }}' dockerkafkastorm_nimbus_1)

docker run -it --rm \
        -e TOPOLOGY_NAME=$1 \
        -e NIMBUS_HOST=$NIMBUS_HOST \
        -e NIMBUS_THRIFT_PORT=$NIMBUS_THRIFT_PORT \
        --name topology \
        storm-topology \
        "kill"

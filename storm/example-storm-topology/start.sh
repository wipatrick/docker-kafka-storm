#!/bin/bash

case "$1" in
        submit)
            /usr/bin/storm jar example-storm-topology-1.0-SNAPSHOT-jar-with-dependencies.jar $MAINCLASS $TOPOLOGY_NAME $ZK_HOST $ZK_PORT $TOPIC -c nimbus.host=$NIMBUS_HOST -c nimbus.thrift.port=$NIMBUS_THRIFT_PORT
            ;;

        kill)
            /usr/bin/storm kill $TOPOLOGY_NAME -c nimbus.host=$NIMBUS_HOST -c nimbus.thrift.port=$NIMBUS_THRIFT_PORT -w 1
            ;;

        *)
            echo $"Usage: $0 {submit|kill}"
            exit 1

esac

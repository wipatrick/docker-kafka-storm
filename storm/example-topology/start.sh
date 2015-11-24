#!/bin/bash

/usr/bin/storm jar biggis-storm-1.0-jar-with-dependencies.jar $MAINCLASS $TOPOLOGY_NAME $ZK_HOST $ZK_PORT $TOPIC -c nimbus.host=$NIMBUS_HOST -c nimbus.thrift.port=$NIMBUS_THRIFT_PORT

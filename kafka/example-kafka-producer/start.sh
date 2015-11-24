#!/bin/bash

java -jar target/example-kafka-producer-1.0-SNAPSHOT-jar-with-dependencies.jar $BROKER_HOST $BROKER_PORT $TOPIC $COUNTER_END $SLEEP_TIME_IN_MILLIS

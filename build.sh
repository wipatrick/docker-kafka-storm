#!/bin/bash

docker build -t=kafka-producer kafka/example-kafka-producer
docker build -t=storm-topology storm/example-storm-topology

docker-compose build

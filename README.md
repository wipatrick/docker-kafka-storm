## docker-kafka-storm
Dockerized Big Data Stream Processing Pipeline for Analyzing Data with [Apache Kafka](http://kafka.apache.org/), [Apache Storm](http://storm.apache.org/). As a minimal working example, a simple wordcount was implemented. Thereby, a ```KafkaProducer``` randomly selects a sentence and publishes it to a single Kafka Broker. Within the ```WordCountTopology``` a ```KafkaSpout``` subscribes to the specific topic and reads off the commit log of the Kafka Broker and consumes the messages, i.e. the sentences. The storm topology then tokenizes the sentence in the ```SplitterBolt```, counts the words in the ```CounterBolt```, ranks them in the ```RankerBolt``` and finally write them to stdout in the ```WriterBolt```.

## About
Data stream processing is becoming incredibly popular mainly because of the era of Big Data and the arise of Frameworks such as [Apache Storm](http://storm.apache.org/) that allow distributed realtime computation. Non-functional requirements often demand a highly-available, fault-tolerant, high-throughput and massively scalable solution. In this context, people intend to use a publish-subscribe messaging system such as [Apache Kafka](http://kafka.apache.org/) as a broker between various data sources (i.e. publisher or producer) and data sinks (i.e. subscriber or consumer) in order to decouple these components. This project serves as a starting point to getting started with Apache Kafka and Apache Storm.

I highly recommend to read both, the [Storm docs](http://storm.apache.org/documentation.html) and [Kafka docs](http://kafka.apache.org/documentation.html) in order to get to know the architecture.

## Prerequisites and Setup
This project has been tested with the following setup:
* Docker Engine 1.9.0
* Docker Compose 1.5.0
* Virtual Box 5.0.8

For the ease of getting started on Windows & Mac OSx, these users should make use of the [Docker Toolbox](https://www.docker.com/docker-toolbox) in order to create a running Docker Host (Docker Machine) instance with [Docker Engine](https://www.docker.com/docker-engine) as well as [Docker Compose](https://www.docker.com/docker-compose) already installed on your desired virtual environment provider.

For this project the following versions of Apache Kafka, Apache Zookeeper and Apache Storm are used:
* Apache Kafka 0.8.2.1
* Apache Zookeeper 3.4.6
* Apache Storm 0.9.4

## Getting Started

```
git clone https://github.com/wipatrick/docker-kafka-storm.git
cd docker-kafka-storm
```

```
docker run -it --rm \
           --name=test \
           ches/kafka \
           /bin/bash -c "/kafka/bin/kafka-console-consumer.sh --zookeeper $(docker-machine ip <NameOfDockerMachine>):2181 --topic test --from-beginning"
```



## Credits
Credits belong to the work of [wurstmeister](https://github.com/wurstmeister) and [ches](https://github.com/ches) for putting [Apache Storm](https://github.com/wurstmeister/storm-docker) and [Apache Kafka](https://github.com/ches/kafka) in a Docker container. Check their repositories on GitHub.

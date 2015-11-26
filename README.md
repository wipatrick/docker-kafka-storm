## docker-kafka-storm
Dockerized Big Data Stream Processing Pipeline for Analyzing Data with [Apache Kafka](http://kafka.apache.org/), [Apache Storm](http://storm.apache.org/). As a minimal working example, a simple wordcount was implemented. Thereby, a ```KafkaProducer``` randomly selects a sentence and publishes it to a single Kafka Broker. Within the ```WordCountTopology``` a ```KafkaSpout``` subscribes to the specific topic and reads off the commit log of the Kafka Broker and consumes the messages, i.e. the sentences. The storm topology then tokenizes the sentence in the ```SplitterBolt```, counts the words in the ```CounterBolt```, ranks them in the ```RankerBolt``` according to their counts and finally logs them.

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
***NOTE***: It is recommended to use a terminal multiplexer such as [tmux](https://tmux.github.io/).

Start off by cloning the repository to your local workplace.
```
git clone https://github.com/wipatrick/docker-kafka-storm.git
cd docker-kafka-storm
```

The ```build.sh``` executes the Dockerfiles stored under kafka/example-kafka-producer/ for building the ***kafka-producer*** and under storm/example-storm-topology for building the ***storm-topology*** image as well as the images in specified within the ```docker-compose.yml``` file. If you make changes to the source code of either the ```KafkaProducer``` or the ```WordCountTopology``` after you have initially build the images, you have to rebuild the corresponding image by setting the option to ```kafka-producer``` or ```storm-topology```, e.g. you made changes to the ```KafkaProducer```, so you would have to ```./build.sh kafka-producer```.
```
➜  docker-kafka-storm git:(master) ./build.sh
Usage: ./build.sh {initial|kafka-producer|storm-topology}
➜  docker-kafka-storm git:(master) ./build.sh initial
```

Once the images are built, you can start the multi-container application stack by running ```compose.sh``` with one of two options. To run in foreground choose ```start-foreground```. To run in detached mode choose ```start-background```. For debugging and learning purposes it is good to run in the foreground.
```
➜  docker-kafka-storm git:(master) ./compose.sh
Usage: ./compose.sh {start-foreground|start-background|stop}
➜  docker-kafka-storm git:(master) ./compose.sh start-foreground
Creating dockerkafkastorm_zookeeper_1
Creating dockerkafkastorm_nimbus_1
Creating dockerkafkastorm_stormui_1
Creating dockerkafkastorm_supervisor_1
Creating dockerkafkastorm_kafka_1
...
```

Once the multi-container application stack is up and running you first need to create a ***topic*** to which the messages are published to. Additionally you will have to pass the ***replication-factor*** and the number of ***partions*** as arguments, e.g. ```./create-kafka-topic.sh <replication-factor> <partition> <topic>```.
```
➜  docker-kafka-storm git:(master) ./create-kafka-topic.sh 1 1 wordcount
Created topic "wordcount".
```

Then, the ```WordCountTopology``` is submitted to the "cluster" by calling ```submit-storm-topology.sh```. Additionally you will have to pass the ***com.example.MainClass***, ***topology-name*** and ***topic*** as arguments, e.g. ```./submit-storm-topology.sh <com.example.MainClass> <topology-name> <topic>```. You can go and check the Storm UI http://DOCKER_HOST_IP:8080 and see the deployed topology.

***NOTE***: **Sometimes it takes a bit for the Storm UI to successfully load.**
```
➜  docker-kafka-storm git:(master) ./submit-storm-topology.sh com.biggis.storm.WordCountTopology wordcount-topology wordcount
319  [main] INFO  com.biggis.storm.WordCountTopology - Submitting topology wordcount-topology to remote cluster.
...
1258 [main] INFO  backtype.storm.StormSubmitter - Successfully uploaded topology jar to assigned location: ...
...
1860 [main] INFO  backtype.storm.StormSubmitter - Finished submitting topology: wordcount-topology
```

Start the ```KafkaProducer``` by executing the ```start-kafka-producer.sh```. Additionally you will have to pass the ***topic***, ***number of produced messages*** and the ***time in milliseconds between two produced messages*** as arguments, e.g. ```./start-kafka-producer.sh <topic> <count> <sleepTimeInMillis>```.
```
➜  docker-kafka-storm git:(master) ./start-kafka-producer.sh wordcount 5000 500
...
2015-11-25 21:43:31 INFO  ClientUtils$:68 - Fetching metadata from broker id:0,host:172.17.0.6,port:9092 with correlation id 0 for 1 topic(s) Set(wordcount)
2015-11-25 21:43:31 INFO  SyncProducer:68 - Connected to 172.17.0.6:9092 for producing
2015-11-25 21:43:31 INFO  SyncProducer:68 - Disconnecting from 172.17.0.6:9092
2015-11-25 21:43:31 INFO  SyncProducer:68 - Connected to 172.17.0.6:9092 for producing
2015-11-25 21:44:00 INFO  Producer:68 - Shutting down producer
2015-11-25 21:44:00 INFO  ProducerPool:68 - Closing all sync producers
2015-11-25 21:44:00 INFO  SyncProducer:68 - Disconnecting from 172.17.0.6:9092
2015-11-25 21:44:00 INFO  Producer:68 - Producer shutdown completed in 35 ms
```

You can check the computation output by executing ```show-storm-output.sh``` which shows the log output from within the supervisor container on ```/var/log/storm/worker-6702.log```.
```
➜  docker-kafka-storm git:(master) ./show-storm-output.sh
...
2015-11-25 21:46:27 c.b.s.b.RankerBolt [INFO] [the=1569, and=770, seven=770, snow=417, white=417, dwarfs=417, cow=385, doctor=385, over=385, keeps=385, away=385, apple=385, an=385, day=385, moon=384, a=384, jumped=384, score=353, four=353, years=353, ago=353, nature=350, with=350, at=350, i=350, two=350, am=350]
...
```

You can kill the Storm topology by executing ```kill-storm-topology.sh``` and passing the ***topology-name*** as an argument, e.g. ```kill-storm-topology.sh <topology-name>```.
```
➜  docker-kafka-storm git:(master) ./kill-storm-topology.sh wordcount-topology
...
1347 [main] INFO  backtype.storm.thrift - Connecting to Nimbus at 172.17.0.3:6627
1499 [main] INFO  backtype.storm.command.kill-topology - Killed topology: wordcount-topology
```

To stop the running multi-container application stack execute ```compose.sh``` again, but this time with ```stop``` as the option.
```
➜  docker-kafka-storm git:(master) ./compose.sh stop
Stopping dockerkafkastorm_kafka_1 ... done
Stopping dockerkafkastorm_supervisor_1 ... done
Stopping dockerkafkastorm_stormui_1 ... done
Stopping dockerkafkastorm_nimbus_1 ... done
Stopping dockerkafkastorm_zookeeper_1 ... done
```
## Optional scripts
Optionally, you can see the produced messages by starting a Kafka consumer in the console an subscribing to the specified topic.
```
➜  docker-kafka-storm git:(master) ./start-kafka-console-consumer.sh wordcount
the cow jumped over the moon
snow white and the seven dwarfs
an apple a day keeps the doctor away
...
```

Furthermore, to clean up the Exited Containers you can execute ```cleanup.sh``` which also removes so called [dangling volumes](http://www.projectatomic.io/blog/2015/07/what-are-docker-none-none-images/).
```
➜  docker-kafka-storm git:(master) docker ps -a
CONTAINER ID        IMAGE                         COMMAND                  CREATED             STATUS                        PORTS               NAMES
77c0b9643af7        dockerkafkastorm_kafka        "/start.sh"              33 seconds ago      Exited (137) 10 seconds ago                       dockerkafkastorm_kafka_1
fcdde25d83c8        dockerkafkastorm_supervisor   "/bin/sh -c /usr/bin/"   33 seconds ago      Exited (137) 10 seconds ago                       dockerkafkastorm_supervisor_1
8ddbb82fe8cf        dockerkafkastorm_stormui      "/bin/sh -c /usr/bin/"   34 seconds ago      Exited (137) 10 seconds ago                       dockerkafkastorm_stormui_1
f580b44212bc        dockerkafkastorm_nimbus       "/bin/sh -c /usr/bin/"   34 seconds
➜  docker-kafka-storm git:(master) ./cleanup.sh
77c0b9643af7
fcdde25d83c8
8ddbb82fe8cf
f580b44212bc
86c5a3bb53ed
➜  docker-kafka-storm git:(master) docker ps -a
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES

```


## Credits
Credits belong to the work of [wurstmeister](https://github.com/wurstmeister) and [ches](https://github.com/ches) for putting [Apache Storm](https://github.com/wurstmeister/storm-docker) and [Apache Kafka](https://github.com/ches/kafka) in a Docker container. Check their repositories on GitHub.

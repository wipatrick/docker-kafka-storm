FROM ubuntu:trusty

MAINTAINER wipatrick

RUN apt-get update && \
    apt-get install -y unzip wget openjdk-7-jre-headless python

RUN wget -q -O - http://mirrors.sonic.net/apache/storm/apache-storm-0.9.4/apache-storm-0.9.4.tar.gz | tar -xzf - -C /opt

ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64/
ENV STORM_HOME /opt/apache-storm-0.9.4

RUN groupadd storm; useradd --gid storm --home-dir /home/storm --create-home --shell /bin/bash storm; chown -R storm:storm $STORM_HOME; mkdir /var/log/storm ; chown -R storm:storm /var/log/storm
RUN ln -s $STORM_HOME/bin/storm /usr/bin/storm

ENV MAINCLASS WordCountTopology
ENV TOPOLOGY_NAME wordcount-topology
ENV ZK_HOST 192.168.99.100
ENV ZK_PORT 2181
ENV TOPIC wordcount
ENV NIMBUS_HOST 192.168.99.100
ENV NIMBUS_THRIFT_PORT 49627

COPY target/biggis-storm-1.0-jar-with-dependencies.jar /usr/bin/
COPY start.sh /usr/bin/

WORKDIR /usr/bin/
ENTRYPOINT ["start.sh"]

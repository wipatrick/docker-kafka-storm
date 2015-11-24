FROM ubuntu:trusty

MAINTAINER wipatrick

RUN apt-get update && \
    apt-get install -y unzip wget openjdk-7-jre-headless python

RUN wget -q -O - http://mirrors.sonic.net/apache/storm/apache-storm-0.9.4/apache-storm-0.9.4.tar.gz | tar -xzf - -C /opt

ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64/
ENV STORM_HOME /opt/apache-storm-0.9.4

RUN groupadd storm; useradd --gid storm --home-dir /home/storm --create-home --shell /bin/bash storm; chown -R storm:storm $STORM_HOME; mkdir /var/log/storm ; chown -R storm:storm /var/log/storm
RUN ln -s $STORM_HOME/bin/storm /usr/bin/storm

COPY target/example-storm-topology-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/bin/example-storm-topology-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY start.sh /usr/bin/start.sh

WORKDIR /usr/bin/
ENTRYPOINT ["start.sh"]

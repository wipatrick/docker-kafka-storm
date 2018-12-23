FROM maven:openjdk:12-jdk

MAINTAINER wipatrick

RUN apt-get update && \
    apt-get install -y unzip wget

RUN wget -q -O - https://archive.apache.org/dist/storm/apache-storm-0.9.4/apache-storm-0.9.4.tar.gz | tar -xzf - -C /opt

ENV STORM_HOME /opt/apache-storm-0.9.4

RUN groupadd storm; useradd --gid storm --home-dir /home/storm --create-home --shell /bin/bash storm; chown -R storm:storm $STORM_HOME; mkdir /var/log/storm ; chown -R storm:storm /var/log/storm
RUN ln -s $STORM_HOME/bin/storm /usr/bin/storm

RUN mkdir --parents /usr/src/storm-topology
WORKDIR /usr/src/storm-topology

# Add POM
ADD pom.xml /usr/src/storm-topology

# Get all dependencies
RUN mvn verify clean --fail-never

# Add rest of code & build
ADD . /usr/src/storm-topology
RUN mvn verify

ADD start.sh /usr/src/storm-topology/start.sh
ENTRYPOINT ["/usr/src/storm-topology/start.sh"]

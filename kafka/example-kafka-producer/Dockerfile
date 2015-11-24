FROM maven:3.2.5-jdk-8u40

MAINTAINER wipatrick

RUN mkdir --parents /usr/src/kafka-producer
WORKDIR /usr/src/kafka-producer

# Add POM
ADD pom.xml /usr/src/kafka-producer

# Get all dependencies
RUN mvn verify clean --fail-never

# Add rest of code & build
ADD . /usr/src/kafka-producer
RUN mvn verify

ADD start.sh /usr/src/kafka-producer/start.sh
ENTRYPOINT ["/usr/src/kafka-producer/start.sh"]

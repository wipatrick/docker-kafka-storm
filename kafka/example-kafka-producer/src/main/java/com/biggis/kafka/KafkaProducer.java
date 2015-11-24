package com.biggis.kafka;

import com.biggis.kafka.helper.Utils;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import java.util.Properties;

/**
 * KafkaProducer Example
 *
 * Find explanations in the links below:
 *
 * http://kafka.apache.org/documentation.html
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 */
public class KafkaProducer {

    static boolean runThread;
    static int COUNTER_START;
    static int COUNTER_END;
    static int SLEEP_TIME_IN_MILLIS;
    static String BROKER_HOST;
    static String BROKER_PORT;
    static String TOPIC;
    static String PARTITION_KEY;
    static ProducerConfig conf;
    static Producer<String, String> producer;

    static String [] sentences = {
            "the cow jumped over the moon",
            "an apple a day keeps the doctor away",
            "four score and seven years ago",
            "snow white and the seven dwarfs",
            "i am at two with nature",
            "a picture is worth a thousand words"
    };

    public static void main(String [] args) {

        /**
         * @BROKER_HOST:            IP of Docker Host (mainly VM) where dockerized Broker instance is running.
         * @BROKER_PORT:            Mapped port of dockerized Broker instance on Host (VM).
         * @TOPIC:                  Published topic to which a consumer (e.g. Apache Storm Kafka Spout) subscribes
         *                          in order to receive messages.
         * @COUNTER_END:            Count of published messages.
         * @SLEEP_TIME_IN_MILLIS:   Amount of time in between to messages.
         */
        BROKER_HOST = args[0];
        BROKER_PORT = args[1];
        TOPIC = args[2];
        COUNTER_END = Integer.parseInt(args[3]);
        SLEEP_TIME_IN_MILLIS = Integer.parseInt(args[4]);

        COUNTER_START = 0;
        runThread = true;
        //PARTITION_KEY = "wordcount";

        Properties props = new Properties();
        props.put("metadata.broker.list", BROKER_HOST + ":" + BROKER_PORT);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        conf = new ProducerConfig(props);
        producer = new Producer<String, String>(conf);

        Thread thread = new Thread() {
            public void run() {
                while (runThread){
                    if(COUNTER_START < COUNTER_END) {

                        String sentence = sentences[(int)(Math.random()*(sentences.length-1))];

                        /**
                         * Without PARTITION_KEY
                         *
                         * Note that if you do not include a key, even if you've defined a partitioner class,
                         * Kafka will assign the message to a random partition.
                         *
                         *
                         * With PARTITION_KEY
                         *
                         * KeyedMessage<String, String> message = new KeyedMessage<String, String>(TOPIC, PARTITION_KEY, sentence);
                         */
                        KeyedMessage<String, String> message = new KeyedMessage<String, String>(TOPIC, sentence);

                        // start sending messages to Kafka Cluster
                        producer.send(message);
                        COUNTER_START++;

                        Utils.waitForMillis(SLEEP_TIME_IN_MILLIS);
                    }
                    else {
                        runThread = false;
                    }
                }
                producer.close();
            }
        };
        thread.start();
    } // end main()
} // end

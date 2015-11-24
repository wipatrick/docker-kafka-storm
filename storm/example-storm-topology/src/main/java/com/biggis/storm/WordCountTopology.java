package com.biggis.storm;

import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.Config;
import backtype.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.*;

/**
 * WordCountTopology
 *
 * The Storm topology "wires" the various computation steps
 * done in the Spout and Bolts together to a DAG via defined
 * stream groupings (shuffle grouping, field grouping, global
 * grouping).
 */
public class WordCountTopology {

    private static final Logger LOG = LoggerFactory.getLogger(WordCountTopology.class);
    private static final String DATASOURCE_SPOUT_ID = "datasource-spout";
    private static final String KAFKA_SPOUT_ID = "kafka-spout";
    private static final String SPLITTER_BOLT_ID = "splitter-bolt";
    private static final String COUNTER_BOLT_ID = "counter-bolt";
    private static final String RANKER_BOLT_ID = "ranker-bolt";
    private static final String WRITER_BOLT_ID = "writer-bolt";
    private static final String TOPOLOGY_NAME = "geo-topology";
    private BrokerHosts brokerHosts;


    public WordCountTopology(String ZK_HOST, String ZK_PORT) {
        brokerHosts = new ZkHosts(ZK_HOST + ":" + ZK_PORT);
    }

    public WordCountTopology() {

    }

    /**
     * WordCountTopology with Kafka
     *
     * @return      StormTopology Object
     */
    public StormTopology buildTopology(String TOPIC) {

        SpoutConfig kafkaConf = new SpoutConfig(brokerHosts, TOPIC, "", "storm");
        kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(KAFKA_SPOUT_ID, new KafkaSpout(kafkaConf));
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4)
                .shuffleGrouping(KAFKA_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4)
                .setNumTasks(4)
                .fieldsGrouping(SPLITTER_BOLT_ID, new Fields("words"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt())
                .globalGrouping(COUNTER_BOLT_ID);
        builder.setBolt(WRITER_BOLT_ID, new WriterBolt())
                .globalGrouping(RANKER_BOLT_ID);

        return builder.createTopology();
    }


    /**
     * WordCountTopology without Kafka
     *
     * @return      StormTopology Object
     */
    public StormTopology buildTopology() {


        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(DATASOURCE_SPOUT_ID, new DataSourceSpout());
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4)
                .shuffleGrouping(DATASOURCE_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4)
                .setNumTasks(4)
                .fieldsGrouping(SPLITTER_BOLT_ID, new Fields("words"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt())
                .globalGrouping(COUNTER_BOLT_ID);
        builder.setBolt(WRITER_BOLT_ID, new WriterBolt())
                .globalGrouping(RANKER_BOLT_ID);

        return builder.createTopology();
    }

    public static void main(String[] args) throws Exception {

        Config conf = new Config();

        if (args != null && args.length > 0) {

            /**
             * Remote deployment as part of Docker Compose multi-application setup
             *
             * @TOPOLOGY_NAME:       Name of Storm topology
             * @ZK_HOST:             Host IP address of ZooKeeper
             * @ZK_PORT:             Port of ZooKeeper
             * @TOPIC:               Kafka Topic which this Storm topology is consuming from
             */
            LOG.info("Submitting topology " + TOPOLOGY_NAME + " to remote cluster.");
            String ZK_HOST = args[0];
            String ZK_PORT = args[1];
            String TOPIC = args[2];

            conf.setDebug(false);
            conf.setNumWorkers(3);

            WordCountTopology wordCountTopology = new WordCountTopology(ZK_HOST, ZK_PORT);
            StormSubmitter.submitTopology(TOPOLOGY_NAME, conf, wordCountTopology.buildTopology(TOPIC));

        }
        else {
            /**
             * Local mode (only for testing purposes)
             */
            LOG.info("Starting topology " + TOPOLOGY_NAME + " in LocalMode.");

            conf.setDebug(false);
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(TOPOLOGY_NAME, conf, new WordCountTopology().buildTopology());

            Thread.sleep(15000);
            cluster.shutdown();
        }
    }
}


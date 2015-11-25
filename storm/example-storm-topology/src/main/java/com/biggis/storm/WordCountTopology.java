package com.biggis.storm;

import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.Config;
import backtype.storm.tuple.Fields;
import com.biggis.storm.bolt.CounterBolt;
import com.biggis.storm.bolt.RankerBolt;
import com.biggis.storm.bolt.SplitterBolt;
import com.biggis.storm.spout.DataSourceSpout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.kafka.*;

import java.util.Arrays;

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
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4).shuffleGrouping(KAFKA_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4).fieldsGrouping(SPLITTER_BOLT_ID, new Fields("word"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt()).globalGrouping(COUNTER_BOLT_ID);

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
        builder.setBolt(SPLITTER_BOLT_ID, new SplitterBolt(), 4).shuffleGrouping(DATASOURCE_SPOUT_ID);
        builder.setBolt(COUNTER_BOLT_ID, new CounterBolt(), 4).fieldsGrouping(SPLITTER_BOLT_ID, new Fields("word"));
        builder.setBolt(RANKER_BOLT_ID, new RankerBolt()).globalGrouping(COUNTER_BOLT_ID);

        return builder.createTopology();
    }

    public static void main(String[] args) throws Exception {

        Config conf = new Config();
        String TOPOLOGY_NAME;

        if (args != null && args.length > 0) {
            TOPOLOGY_NAME = args[0];
            /**
             * Remote deployment as part of Docker Compose multi-application setup
             *
             * @TOPOLOGY_NAME:       Name of Storm topology
             * @ZK_HOST:             Host IP address of ZooKeeper
             * @ZK_PORT:             Port of ZooKeeper
             * @TOPIC:               Kafka Topic which this Storm topology is consuming from
             */
            LOG.info("Submitting topology " + TOPOLOGY_NAME + " to remote cluster.");
            String ZK_HOST = args[1];
            int ZK_PORT = Integer.parseInt(args[2]);
            String TOPIC = args[3];
            String NIMBUS_HOST = args[4];
            int NIMBUS_THRIFT_PORT = Integer.parseInt(args[5]);

            conf.setDebug(false);
            conf.setNumWorkers(2);
            conf.setMaxTaskParallelism(5);
            conf.put(Config.NIMBUS_HOST, NIMBUS_HOST);
            conf.put(Config.NIMBUS_THRIFT_PORT, NIMBUS_THRIFT_PORT);
            conf.put(Config.STORM_ZOOKEEPER_PORT, ZK_PORT);
            conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(ZK_HOST));

            WordCountTopology wordCountTopology = new WordCountTopology(ZK_HOST, String.valueOf(ZK_PORT));
            StormSubmitter.submitTopology(TOPOLOGY_NAME, conf, wordCountTopology.buildTopology(TOPIC));

        }
        else {
            TOPOLOGY_NAME = "wordcount-topology";
            /**
             * Local mode (only for testing purposes)
             */
            LOG.info("Starting topology " + TOPOLOGY_NAME + " in LocalMode.");

            conf.setDebug(false);
            conf.setNumWorkers(2);
            conf.setMaxTaskParallelism(2);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(TOPOLOGY_NAME, conf, new WordCountTopology().buildTopology());

            Thread.sleep(10000);
            cluster.shutdown();
        }
    }
}


package com.biggis.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SplitterBolt
 */
public class SplitterBolt extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(SplitterBolt.class);
    private OutputCollector collector;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        this.collector = outputCollector;
    }

    public void execute(Tuple tuple) {

        // get tuples from KafkaSpout
        String [] words = tuple.getString(0).split("\\s+");

        for (int i = 0; i < words.length; i++) {
            // check fo non-word character and replace
            words[i] = words[i].replaceAll("[^\\w]", "");

            // emit words and acknowledge processed tuple
            collector.emit(tuple, new Values(words[i]));
            collector.ack(tuple);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }
}

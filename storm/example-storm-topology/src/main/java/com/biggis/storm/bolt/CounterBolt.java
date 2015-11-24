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

import java.util.HashMap;
import java.util.Map;

/**
 * CounterBolt
 */
public class CounterBolt extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(CounterBolt.class);
    private OutputCollector collector ;
    private Map<String, Integer> counts;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        this.collector = outputCollector;
        this.counts = new HashMap<String, Integer>();
    }

    public void execute(Tuple tuple) {

        String word = tuple.getStringByField("word");

        Integer count = counts.get(word);
        if (count == null){
            count = 0;
        }
        count++;

        counts.put(word, count);
        collector .emit(tuple, new Values(word, count));
        collector .ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word", "count"));
    }
}

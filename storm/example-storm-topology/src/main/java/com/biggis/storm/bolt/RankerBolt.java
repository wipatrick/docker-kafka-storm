package com.biggis.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * RankerBolt
 */
public class RankerBolt extends BaseRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(RankerBolt.class);
    private OutputCollector collector ;
    private Map<String, Integer> rankerMap;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

        this.collector = outputCollector;
        this.rankerMap = new HashMap<String, Integer>();
    }

    public void execute(Tuple tuple) {

        String word = tuple.getStringByField("word");
        Integer count = tuple.getIntegerByField("count");

        rankerMap.put(word, count);

        LOG.info(sortEntriesByCount(rankerMap).toString());
        collector.ack(tuple);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    /**
     * helper method
     *
     * http://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return      sorted entries according to their count
     */
    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> sortEntriesByCount(Map<K,V> map) {
        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map. entrySet());
        Collections. sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                });
        return sortedEntries ;
    }
}

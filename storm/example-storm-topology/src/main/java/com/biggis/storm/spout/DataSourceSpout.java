package com.biggis.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.biggis.storm.helper.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * DataSourceSpout
 */
public class DataSourceSpout extends BaseRichSpout {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceSpout.class);
    private SpoutOutputCollector collector;
    private Random rand;
    private String [] sentences = {
            "the cow jumped over the moon",
            "an apple a day keeps the doctor away",
            "four score and seven years ago",
            "snow white and the seven dwarfs",
            "i am at two with nature",
            "a picture is worth a thousand words",
            "there is no place like home",
            "the early bird catches the worm",
            "practice makes perfect",
            "easy come easy go",
            "all good things must come to an end",
            "beauty is in the eye of the beholder",
            "you cannot judge a book by its cover",
            "the grass is always greener on the other side of the hill"
    };

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {

        this.collector = spoutOutputCollector;
        this.rand = new Random();
    }

    public void nextTuple() {

        String sentence = sentences[(int)(Math.random()*(sentences.length-1))];
        collector.emit(new Values(sentence));

        Utils.waitForMillis(500);
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare (new Fields("sentence"));
    }
}

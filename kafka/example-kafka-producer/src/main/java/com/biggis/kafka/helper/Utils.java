package com.biggis.kafka.helper;

/**
 * Utils
 *
 * waitForSeconds(int seconds)
 * waitForMillis (long milliseconds)
 *
 * NOTE: LOCAL-MODE ONLY
 *
 * Defines how much time is spent between two emits
 * of sentences in KafkaProducer
 */
public class Utils {

    public static void waitForSeconds (int seconds) {
        try{
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {

        }
    }

    public static void waitForMillis (long milliseconds) {
        try{
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {

        }
    }
}

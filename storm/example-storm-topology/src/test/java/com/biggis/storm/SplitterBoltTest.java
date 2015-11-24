package com.biggis.storm;

/**
 * Created by wipatrick on 24.11.15.
 */
public class SplitterBoltTest {
    public static void main (String [] args ){
        String s = "This is a sample sentence.";
        String[] words = s.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            words[i] = words[i].replaceAll("[^\\w]", "");
            System.out.println(words[i]);
        }
    }
}

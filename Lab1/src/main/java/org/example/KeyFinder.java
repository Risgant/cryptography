package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class KeyFinder {
    private static final Map<Character, Double> lettersFreq = Map.ofEntries(
            entry('a', 0.08167), entry('b', 0.01492), entry('c', 0.02782), entry('d', 0.04253),
            entry('e', 0.12702), entry('f', 0.0228), entry('g', 0.02015), entry('h', 0.06094),
            entry('i', 0.06966), entry('j', 0.00153), entry('k', 0.00772), entry('l', 0.04025),
            entry('m', 0.02406), entry('n', 0.06749), entry('o', 0.07507), entry('p', 0.01929),
            entry('q', 0.00095), entry('r', 0.05987), entry('s', 0.06327), entry('t', 0.09056),
            entry('u', 0.02758), entry('v', 0.00978), entry('w', 0.0236), entry('x', 0.0015),
            entry('y', 0.01974), entry('z', 0.00074)
    );

    public static String findKey(String encodedText, int keyLength) {
        List<Map<Character, Integer>> encodedLettersCount = new ArrayList<>();
        List<Map<Character, Double>> encodedLettersFreq = new ArrayList<>();
        List<Integer> allLettersCounts = new ArrayList<>();
        for(int i = 0; i< keyLength; ++i) {
            allLettersCounts.add(0);
            Map<Character, Integer> countsMap = new HashMap<>();
            Map<Character, Double> freqMap = new HashMap<>();
            for(char j = 'a'; j <= 'z'; ++j) {
                countsMap.put(j, 0);
            }
            encodedLettersCount.add(countsMap);
            encodedLettersFreq.add(freqMap);
        }
        int nonLetter = 0;
        for(int i = 0; i < encodedText.length(); ++i) {
            int idx = i % keyLength;
            char ch = encodedText.charAt(i);
            if(Utils.isLetter(ch)) {
                allLettersCounts.set(idx, allLettersCounts.get(idx)+1);
                char lowerCh = Character.toLowerCase(ch);
                Map<Character, Integer> map = encodedLettersCount.get(idx);
                int letterCount = map.get(lowerCh);
                map.put(lowerCh, letterCount + 1);
            } else {
                ++nonLetter;
            }
        }

        for(int i = 0; i < keyLength; ++i) {
            int allLettersCount = allLettersCounts.get(i);
            for(Map.Entry<Character, Integer> entry: encodedLettersCount.get(i).entrySet()) {
                encodedLettersFreq.get(i).put(entry.getKey(), (double)entry.getValue()/((double)allLettersCount));
            }
        }

        StringBuilder keyBuilder = new StringBuilder();
        for(int i = 0; i < keyLength; ++i) {
            double maxProb = 0;
            int closestOffset = 0;
            for(int startOffset = 0; startOffset < 'z'-'a'; ++startOffset) {
                double prob = 1;
                for(Map.Entry<Character, Double> letterFreq: lettersFreq.entrySet()) {
                    char encoded = Cipher.cipherLetter(letterFreq.getKey(), startOffset, Cipher.encodingOffsetFunc);
                    double encodedLetterFreq =  encodedLettersFreq.get(i).get(encoded);
                    if(encodedLetterFreq != 0.0) {
                        prob*=equalPercent(letterFreq.getValue(), encodedLetterFreq);
                    }
                }
                if(prob > maxProb) {
                    maxProb = prob;
                    closestOffset = startOffset;
                }
            }
            char actualChar = (char) ('a'+closestOffset);
            keyBuilder.append(actualChar);
        }


        return keyBuilder.toString();
    }

    private static double equalPercent(double a, double b) {
        if(a > b) {
            return b/a;
        }
        return a/b;
    }
}

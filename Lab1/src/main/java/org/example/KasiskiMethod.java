package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KasiskiMethod {

    public static int findKeyLength(String encryptedText, int lGrammLength) {
        Map<Integer, Integer> lGrammsCounts = new HashMap<>();
        int maxCount = 0;
        for(int i = 0; i < encryptedText.length() - lGrammLength; ++i) {
            String subStr = encryptedText.substring(i, i+lGrammLength).toLowerCase(Locale.ROOT);
            if(Utils.isTextual(subStr)) {
                Pattern pattern = Pattern.compile(subStr, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(encryptedText);
                if(matcher.find()) {
                    int prevIdx = matcher.start();
                    while (matcher.find()) {
                        int nextIdx = matcher.start();
                        int length = nextIdx - prevIdx;
                        Integer lGrammsCount = lGrammsCounts.get(length);
                        if(lGrammsCount!= null && lGrammsCount > maxCount) {
                            maxCount = lGrammsCount;
                        }
                        lGrammsCounts.put(length, lGrammsCount == null ? 1 : lGrammsCount + 1);
                    }
                }
            }
        }
        if(lGrammsCounts.size() == 0) {
            return 0;
        }
        int minAvailableCount = maxCount/10;
        Integer[] keyLengths = lGrammsCounts.entrySet().stream()
                .filter(kv -> kv.getValue() > minAvailableCount)
                .map(Map.Entry::getKey)
                .toArray(Integer[]::new);
        return findGCD(keyLengths);
    }

    private static int findGCD(Integer[] array) {
        Map<Integer, Integer> gcds = new HashMap<>();
        for(int i = 1; i < array.length; i++){
            for(int j = i+1; j < array.length; ++j) {
                int gcd = findGCD(array[i], array[j]);
                Integer gcdCount = gcds.get(gcd);
                gcds.put(gcd, gcdCount == null ? 1 : gcdCount + 1);
            }
        }
        return gcds.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(1);
    }

    private static int findGCD(int a, int b){
        if(b == 0)
            return a;
        return findGCD(b, a%b);
    }

    private static boolean isPrime(int num){
        boolean flag = false;
        for (int i = 2; i <= num / 2; ++i) {
            // condition for nonprime number
            if (num % i == 0) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}

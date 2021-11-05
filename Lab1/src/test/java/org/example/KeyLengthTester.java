package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KeyLengthTester {

    public static void main(String[] args) throws IOException {
        String text = Files.readString(Path.of("input.txt"));

        int countOfTries = 30;
        Map<Integer, Double> textSizeByPercent = new HashMap<>();
        for(int keyLength = 2; keyLength <= 30; keyLength+=2) {
            int successTries = 0;
            for(int j = 0; j < countOfTries; ++j) {
                String key = generateKey(keyLength);
                String encodedText = Cipher.cipherText(text, key, Cipher.encodingOffsetFunc);
                int foundKeyLength = KasiskiMethod.findKeyLength(encodedText, 3);
                if(foundKeyLength == keyLength) {
                    String foundKey = KeyFinder.findKey(encodedText, foundKeyLength);
                    if(key.equals(foundKey)) {
                        ++successTries;
                    }
                }
            }
            textSizeByPercent.put(keyLength, (double)successTries/(double)countOfTries);
        }
        FileWriter out = new FileWriter("percents_by_key_length.csv");
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("key length", "success percent");
        for(Map.Entry<Integer, Double> entry: textSizeByPercent.entrySet()) {
            printer.printRecord(entry.getKey(), entry.getValue());
        }
        printer.close();
    }

    private static String generateKey(int length) {
        Random random = new Random();
        int totalOffset = 'z' - 'a';
        StringBuilder keyBuilder = new StringBuilder();
        for(int i = 0; i < length; ++i) {
            char ch = (char)('a'+random.nextInt(totalOffset+1));
            keyBuilder.append(ch);
        }
        return keyBuilder.toString();
    }
}

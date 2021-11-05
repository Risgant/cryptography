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

public class TextSizeTester {

    public static void main(String[] args) throws IOException {
        String text = Files.readString(Path.of("input.txt"));

        int keyLength = 3;
        int countOfTries = 200;
        Map<Integer, Double> textSizeByPercent = new HashMap<>();
        for(int i = 50; i <= 500; i+=10) {
            String subText = text.substring(0, i);
            int successTries = 0;
            for(int j = 0; j < countOfTries; ++j) {
                String key = generateKey(keyLength);
                String encodedSubText = Cipher.cipherText(subText, key, Cipher.encodingOffsetFunc);
                int foundKeyLength = KasiskiMethod.findKeyLength(encodedSubText, 3);
                if(foundKeyLength == keyLength) {
                    String foundKey = KeyFinder.findKey(encodedSubText, foundKeyLength);
                    if(key.equals(foundKey)) {
                        ++successTries;
                    }
                }
            }
            textSizeByPercent.put(subText.length(), (double)successTries/(double)countOfTries);
        }
        FileWriter out = new FileWriter("percents_by_text_size5.csv");
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("size of text", "success percent");
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

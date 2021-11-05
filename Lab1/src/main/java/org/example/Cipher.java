package org.example;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Cipher {

    public static final OffsetFunction encodingOffsetFunc = (offset, ch) -> offset > 'z' - ch ? 'a' - 'z' + offset - 1 : offset;
    public static final OffsetFunction decodingOffsetFunc = (offset, ch) -> offset > ch - 'a' ? 'z' - 'a' - offset + 1 : -offset;

    private static final String[] availableFlags = new String[]{"-e", "-d"};

    public static void main(String[] args) throws IOException {
        int argsCount = args.length;
        if(argsCount > 2 || argsCount == 0|| !isFlagArg(args[0]) || "-e".equals(args[0]) && argsCount == 1) {
            System.out.println("Lab1.exe -e|-d [key]\n");
            System.out.println("-e encrypt\n");
            System.out.println("-d decrypt\n");
            return;
        }
        String text = Files.readString(Path.of("input.txt"));
        String flag = args[0];
        String ciphered;
//        System.out.println("text length: "+text.length());
        if("-e".equals(flag)) {
            String key = args[1].toLowerCase(Locale.ROOT);
            if(!Utils.isTextual(key)) {
                System.out.println("Key should be textual");
                return;
            }
            ciphered = cipherText(text, key, encodingOffsetFunc);
        } else {
            int keyLength = KasiskiMethod.findKeyLength(text.toLowerCase(Locale.ROOT), 3);
            String foundKey = KeyFinder.findKey(text, keyLength);
            System.out.println("found key length: "+keyLength);
            System.out.println("found key: "+foundKey);
            ciphered = cipherText(text, foundKey, decodingOffsetFunc);
        }
        Files.writeString(Path.of("output.txt"), ciphered);
    }

    private static boolean isFlagArg(String arg) {
        return ArrayUtils.contains(availableFlags, arg);
    }

    public static String cipherText(String text, String key, OffsetFunction offsetFunction) {
        StringBuilder strBuilder = new StringBuilder();
        for(int i = 0; i < text.length(); ++i) {
            int offset = key.charAt(i % key.length()) - 'a';
            char cipheredLetter = cipherLetter(text.charAt(i), offset, offsetFunction);
            strBuilder.append(cipheredLetter);
        }
        return strBuilder.toString();
    }

    public static char cipherLetter(char ch, int offset, OffsetFunction offsetFunction) {
        if(Utils.isLetter(ch)) {
            char lowerCase = Character.toLowerCase(ch);
            return (char) (ch + offsetFunction.getActualOffset(offset, lowerCase));
        }
        return ch;
    }
}

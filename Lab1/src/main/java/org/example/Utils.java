package org.example;

public class Utils {

    public static boolean isTextual(String str) {
        return str.chars().allMatch(Utils::isLetter);
    }

    public static boolean isLetter(int ch) {
        return  ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z');
    }
}

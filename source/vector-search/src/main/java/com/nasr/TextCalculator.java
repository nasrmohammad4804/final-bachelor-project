package com.nasr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextCalculator {

    public static List<String> tokenize(String text) {

        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(token -> !token.isEmpty())
                .collect(Collectors.toList());
    }

    public static String removeStopWords(String text) {
        return text.replaceAll("(\\bam\\b)|(\\bis\\b)|(\\ba\\b)|(\\bare\\b)|(\\band\\b)|(\\buses\\b)", "");

    }
}

package com.nasr;

import lombok.Data;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

import static java.util.Collections.singleton;

@Data
public class InvertedIndex {

    private final Map<String, MutablePair<Integer, List<Doc>>> index = new HashMap<>();

    private final Set<Doc> totalDocs = new HashSet<>();

    private final Map<Long, Map<String, Double>> tfidfDocScores = new HashMap<>();

    public void insertDocument(Doc doc) {

        totalDocs.add(doc);

        List<String> tokenize = TextCalculator.tokenize(doc.getText());
        for (String token : tokenize) {

            MutablePair<Integer, List<Doc>> entry = index.get(token);


            if (entry == null) {
                index.put(token, MutablePair.of(1, new ArrayList<>(singleton(doc))));
            } else {
                entry.setLeft(entry.getLeft() + 1);
                entry.getRight().add(doc);
            }
        }
    }

    public void insertAll(List<Doc> docs) {
        docs.forEach(this::insertDocument);
    }

    public Map<String, Double> calculateTFDocument(List<String> tokenize) {

        Set<String> uniqueWords = new HashSet<>(tokenize);

        Map<String, Double> score = new HashMap<>();

        for (String word : uniqueWords) {
            long count = tokenize.stream().filter(t -> t.equals(word)).count();
            score.put(word, (double) count / tokenize.size());
        }
        return score;

    }

    public Map<String, Double> calculateIDFDocument(List<String> tokenize) {


        Set<String> uniqueWords = new HashSet<>(tokenize);

        Map<String, Double> score = new HashMap<>();


        for (String word : uniqueWords) {


            int wordInDocUnit = 0;
            if (Optional.ofNullable(index.get(word)).isPresent())
                wordInDocUnit = index.get(word).getLeft();

            score.put(word, Math.log((1.0 + totalDocs.size()) / (1.0 + wordInDocUnit)) + 1.0);
        }
        return score;

    }

    public Map<String,Double> calculateTFIDFDocument(String text) {
        List<String> tokenize = TextCalculator.tokenize(text);

        Map<String, Double> tfScore = calculateTFDocument(tokenize);
        Map<String, Double> idfScore = calculateIDFDocument(tokenize);

        Map<String, Double> score = new HashMap<>();

        for (Map.Entry<String, Double> entry : tfScore.entrySet()) {

            double value = idfScore.get(entry.getKey()) * entry.getValue();
            score.put(entry.getKey(), value);
        }
        return score;

    }
    public void calculateTFIDFDocument(Doc doc) {

        Map<String, Double> score = calculateTFIDFDocument(doc.getText());
        tfidfDocScores.put(doc.getId(), score);
    }

    public void calculateTFIDFDocuments() {
        for (Doc doc : totalDocs) {
            calculateTFIDFDocument(doc);
        }
    }
}

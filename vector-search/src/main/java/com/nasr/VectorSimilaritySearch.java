package com.nasr;

import java.util.List;
import java.util.Map;


public class VectorSimilaritySearch {

    private final InvertedIndex invertedIndex;

    public VectorSimilaritySearch(List<Doc> docs) {
        invertedIndex = new InvertedIndex();
        invertedIndex.insertAll(docs);
        invertedIndex.calculateTFIDFDocuments();
    }

    public Doc search(String query) {


        return findClosestDoc(query);
    }

    private Doc findClosestDoc(String query) {

        Map<String, Double> queryVector = invertedIndex.calculateTFIDFDocument(query);

        double bestResult = -1;
        long docId = 0;

        for (Map.Entry<Long, Map<String, Double>> entry : invertedIndex.getTfidfDocScores().entrySet()) {

            Map<String, Double> docVector = entry.getValue();

            double result;
            double dotProduct = 0;
            double firstNormVector = 0, secondNormVector = 0;

            for (Map.Entry<String, Double> docEntry : docVector.entrySet()) {

                dotProduct += docEntry.getValue() * queryVector.getOrDefault(docEntry.getKey(), 0d);
                firstNormVector = Math.pow(docEntry.getValue(), 2);
                secondNormVector = Math.pow(docEntry.getValue(), 2);
            }
            result = (dotProduct / (Math.sqrt(firstNormVector) * Math.sqrt(secondNormVector)));
            if (result > bestResult) {
                bestResult = result;
                docId = entry.getKey();
            }


        }
        long finalDocId = docId;

        return invertedIndex.getTotalDocs().stream().filter(doc -> doc.getId().equals(finalDocId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("not founded"));
    }

    public void print() {

        invertedIndex.getTfidfDocScores().forEach((key, value) -> {
            System.out.println("Document " + (key) + ": " + invertedIndex.getTotalDocs().stream().filter(doc -> doc.getId().equals(key)).findFirst().map(Doc::getText) + "\n");
            System.out.println("TF-IDF Vector:");
            value.forEach((term, score) -> {
                System.out.printf("  %s : %.4f%n", term, score);
            });
            System.out.println();
        });

    }


}

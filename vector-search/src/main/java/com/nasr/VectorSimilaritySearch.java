package com.nasr;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.pow;


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

        Set<Long> docIds = invertedIndex.candidateDocumentBaseOnQuery(query);
        Set<Map.Entry<Long, Map<String, Double>>> entries = invertedIndex.tfidfDocScoresById(docIds)
                .entrySet();

        for (Map.Entry<Long, Map<String, Double>> entry : entries) {

            Map<String, Double> docVector = entry.getValue();
            double result = calculateWithCosineSimilarity(queryVector, docVector);

            System.out.println("doc : " + (entry.getKey()) + " vector result is : " + result);
            if (result > bestResult) {
                bestResult = result;
                docId = entry.getKey();
            }


        }
        long finalDocId = docId;

        return invertedIndex.getTotalDocs().stream().filter(doc -> doc.getId().equals(finalDocId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("not founded"));
    }

    public double calculateWithCosineSimilarity(Map<String, Double> docVector, Map<String, Double> queryVector) {

        Set<String> allTerms = new HashSet<>();

        allTerms.addAll(docVector.keySet());
        allTerms.addAll(queryVector.keySet());

        double result;
        double dotProduct = 0d;
        double firstNormVector = 0d, secondNormVector = 0d;


        for (String term : allTerms) {

            double docTermValue = docVector.getOrDefault(term, 0.0);
            double queryTermValue = queryVector.getOrDefault(term, 0.0);

            dotProduct += docTermValue * queryTermValue;
            firstNormVector += pow(docTermValue, 2);
            secondNormVector += pow(queryTermValue, 2);
        }
        if (firstNormVector == 0.0 || secondNormVector == 0.0)
            result = 0;

        else
            result = (dotProduct / (Math.sqrt(firstNormVector) * Math.sqrt(secondNormVector)));

        return result;
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

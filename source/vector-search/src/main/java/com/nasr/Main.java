package com.nasr;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Doc> documents = Arrays.asList(
                new Doc(1L,"Java is a popular programming language."),
                new Doc(2L,"Python is used for data science and machine learning."),
                new Doc(3L,"Java supports object-oriented programming concepts."),
                new Doc(4L,"Machine learning is a part of artificial intelligence.")
        );



        VectorSimilaritySearch vectorSimilaritySearch = new VectorSimilaritySearch(documents);

        vectorSimilaritySearch.print();
        String query = "object oriented Java programming";

        Doc result = vectorSimilaritySearch.search(query);
        System.out.printf("best result is :   %s",result.getText());
    }
}

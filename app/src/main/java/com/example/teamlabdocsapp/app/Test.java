package com.example.teamlabdocsapp.app;

/**
 * Created by facetostool on 10.05.2014.
 */
public class Test {
    public static void main(String[] args) {
        String string = "TeamLab Office Sample Document.docx";
        String[] parts = string.split("\\.");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
    }
}

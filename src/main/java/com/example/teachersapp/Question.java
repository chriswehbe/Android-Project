package com.example.teachersapp;

public class Question {
    private String questiontext;
    private String status;
    private int questionid;

    private int assesmentid;
    public Question(String text) {
        this.questiontext = text;
    }
    public Question() {
        // Empty constructor for Gson
    }

    public int getQuestionid() {
        return questionid;
    }

    public String getText() {
        return questiontext;
    }

    public void setText(String text) {
        this.questiontext = text;
    }
}
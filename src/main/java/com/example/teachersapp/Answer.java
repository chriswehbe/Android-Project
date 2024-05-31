package com.example.teachersapp;

public class Answer {
    private int questionid;
    private String text;
    private int answerid;

    public Answer(int questionid, String text) {
        this.questionid = questionid;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestionid() {
        return questionid;
    }

    public String getText() {
        return text;
    }
}

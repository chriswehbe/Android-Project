package com.example.teachersapp;

public class QuestionType {
    private int questiontypeid;
    private String typename;

    public int getQuestionId() {
        return questiontypeid;
    }

    public String getTypename() {
        return typename;
    }
    @Override
    public String toString() {
        return typename;
    }
}

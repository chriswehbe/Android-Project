package com.example.teachersapp;

public class AssessmentType {
    private int assesmenttypeid;
    private String typename;
    public int getAssessmentTypeId() {
        return assesmenttypeid;
    }

    public String getTypename() {
        return typename;
    }
    @Override
    public String toString() {
        return typename;
    }


}

package com.example.teachersapp;

public class AssessmentItem {
    private String assesmentid;
    private String assesmentName;
    private int assesmenttype;

    private int questiontype;
    private int status;
    private String materialid;
    private String year;
    private String materialname;
    public AssessmentItem(String assesmentid, String assesmentName, int assesmenttype, int questiontype, int status, String materialid, String year, String materialname) {
        this.assesmentid = assesmentid;
        this.assesmentName = assesmentName;
        this.assesmenttype = assesmenttype;
        this.questiontype = questiontype;
        this.status = status;
        this.materialid = materialid;
        this.year = year;
        this.materialname = materialname;
    }
    public String getAssesmentid() {
        return assesmentid;
    }

    public String getAssesmentName() {
        return assesmentName;
    }

    public int getAssesmenttype() {
        return assesmenttype;
    }

    public int getQuestiontype() {
        return questiontype;
    }

    public int getStatus() {
        return status;
    }

    public String getMaterialid() {
        return materialid;
    }

    public String getYear() {
        return year;
    }

    public String getMaterialname() {
        return materialname;
    }



}


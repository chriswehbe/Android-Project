package com.example.teachersapp;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenInfo {

    private String userId;
    private String loggedInAs;
    private boolean isAdmin;


    public TokenInfo(String userId, String loggedInAs, boolean isAdmin) {
        this.userId = userId;
        this.loggedInAs = loggedInAs;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getLoggedInAs() {
        return loggedInAs;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
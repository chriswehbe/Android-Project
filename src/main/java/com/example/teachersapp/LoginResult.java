package com.example.teachersapp;

public class LoginResult {
    //class that will receive the result from backend
    private boolean LoggedIn;
    private String token;
    private boolean isAdmin;
    private String userId;

    public boolean isLoggedIn() {
        return LoggedIn;
    }

    public String getToken() {
        return token;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    public String getUserId(){
        return this.userId;
    }
}

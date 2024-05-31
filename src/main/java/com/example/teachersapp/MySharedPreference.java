package com.example.teachersapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    private SharedPreferences sharedPreferences;
    private static final String KEY_USER_ID = "userId";

    public MySharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    public void saveUserId(String userId) {
        // Save the user ID directly
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        // Retrieve the user ID from SharedPreferences
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public boolean isUserIdAvailable() {
        // Check if the user ID is available in SharedPreferences
        return !getUserId().isEmpty();
    }

    public void clearUserData() {
        // Clear user-related data from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_ID);
        editor.apply();
    }
}
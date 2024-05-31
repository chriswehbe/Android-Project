package com.example.teachersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView toSignup;
    private Button loginButton;
    private Retrofit retrofit;
    private MySharedPreference sharedPreference;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
            sharedPreference = new MySharedPreference(this);
        emailEditText = findViewById(R.id.login_email);
        toSignup = findViewById(R.id.toSignup);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", emailEditText.getText().toString());
                    map.put("password", passwordEditText.getText().toString());
                    Call<LoginResult> call = retrofitInterface.executeLogin(map);
                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            if (response.body() != null) {
                                LoginResult res = response.body();
                                if (res.isLoggedIn()) {
                                    sharedPreference.saveUserId(res.getUserId()); // Save user ID directly
                                    if (res.isAdmin()) {
                                        navigateToAdminHome();
                                    } else {
                                        navigateToUserHome();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Handle unsuccessful response (non-2xx status code)
                                Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Fields must not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignup();
            }
        });

    }
    private void navigateToAdminHome() {
        Intent intent = new Intent(this, AdminHome.class);
        startActivity(intent);
//        finish(); // Optional: Finish the current activity to prevent the user from going back
    }
    private void navigateToUserHome() {
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
//        finish(); // Optional: Finish the current activity to prevent the user from going back
    }
    private void navigateToSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
//        finish(); // Optional: Finish the current activity to prevent the user from going back
    }

}


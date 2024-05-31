package com.example.teachersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText, fNameEditText, lNameEditText, userIDEditText;
    private TextView toLogin;
    private Button signupButton;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        confirmPasswordEditText = findViewById(R.id.signup_confirm);
        fNameEditText = findViewById(R.id.signup_fname);
        lNameEditText = findViewById(R.id.signup_lname);
        userIDEditText = findViewById(R.id.signup_userID);
        toLogin = findViewById(R.id.toLogin);

        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty() &&
                        !userIDEditText.getText().toString().isEmpty() && !fNameEditText.getText().toString().isEmpty() && !lNameEditText.getText().toString().isEmpty()) {
                    if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                        // Validate email pattern
                        if (!isValidEmail(emailEditText.getText().toString())) {
                            Toast.makeText(SignupActivity.this, "Invalid email format", Toast.LENGTH_LONG).show();
                            return;
                        }
                        HashMap<String, String> map = new HashMap<>();
                        map.put("email", emailEditText.getText().toString());
                        map.put("password", passwordEditText.getText().toString());
                        map.put("firstName", fNameEditText.getText().toString());
                        map.put("lastName", lNameEditText.getText().toString());
                        map.put("id", userIDEditText.getText().toString());

                        checkExistingUser(map);
                    } else {
                        Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Fields must not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void checkExistingUser(HashMap<String, String> map) {
        Call<Void> call = retrofitInterface.checkExistingUser(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 500) {
                        signupUser(map);
                    } else if(response.code() ==200) {
                        Toast.makeText(SignupActivity.this, "Email or userID already exists", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signupUser(HashMap<String, String> map) {


        Call<Void> call = retrofitInterface.executeSignup(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signed up successfully.", Toast.LENGTH_LONG).show();
                    navigateToLogin();
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
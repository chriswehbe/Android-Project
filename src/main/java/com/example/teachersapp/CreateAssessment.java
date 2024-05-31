package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAssessment extends AppCompatActivity {
    private String assessmentID;

    private List<Question> questions;
    private EditText questionEditText;
    private TextView errorTextView;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";
    private LinearLayout questionContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assessment);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        assessmentID = getIntent().getStringExtra("assessmentID");

        questions = new ArrayList<>();
        questionEditText = findViewById(R.id.questionEditText);
        errorTextView = findViewById(R.id.errorTextView);
        questionContainer = findViewById(R.id.questionContainer);

        Button addQuestionButton = findViewById(R.id.addQuestionButton);
        Button submitButton = findViewById(R.id.submitButton);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void addQuestion() {
        String questionText = questionEditText.getText().toString().trim();
        if (!questionText.isEmpty()) {
            Question question = new Question(questionText);
            questions.add(question);

            // Create a new TextView dynamically
            TextView questionTextView = new TextView(this);
            questionTextView.setText(questionText);
            questionTextView.setTextSize(16);

            // Add the TextView to the LinearLayout
            questionContainer.addView(questionTextView);

            questionEditText.getText().clear(); // Clear the EditText after adding a question
        }else{
            showError("Text should not be empty");
        }
    }

    private void handleSubmit() {
        if (questions.isEmpty()) {
            showError("Please add at least one question");
        } else {
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("questions", questions);
            requestBody.put("newAssesmentID", assessmentID);
            Call<Void> createAssessment = retrofitInterface.createAssessment(requestBody);
            createAssessment.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(CreateAssessment.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(CreateAssessment.this, "Created successfully", Toast.LENGTH_SHORT).show();
                        navigateToHome();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(CreateAssessment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showError(String errorMessage) {
        errorTextView.setText(errorMessage);
    }
    private void navigateToHome (){
        Intent intent = new Intent(this, AdminHome.class);
        startActivity(intent);
    }
}
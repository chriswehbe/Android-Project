package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachersapp.R;
import com.example.teachersapp.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.List;

public class ResultsView extends AppCompatActivity {

    private RetrofitInterface retrofitInterface;
    private List<QuestionAnswerItem> questions;
    private Retrofit retrofit;
    private String assessmentID;
    private String userID;
    private MySharedPreference sharedPreference;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_view);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        sharedPreference = new MySharedPreference(this);
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the logout method
                handleLogout();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            assessmentID = extras.getString("assessmentID");
            userID = extras.getString("userID");
        }

        fetchQuestionsAndAnswers();
    }

    private void fetchQuestionsAndAnswers() {
        HashMap<String, String> map = new HashMap<>();
        map.put("assessmentID", assessmentID);
        map.put("userID", userID);
        Call<List<QuestionAnswerItem>> call = retrofitInterface.getQuestionsAndAnswers(map);
        call.enqueue(new Callback<List<QuestionAnswerItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuestionAnswerItem>> call, @NonNull Response<List<QuestionAnswerItem>> response) {
                if (response.isSuccessful()) {
                    questions = response.body();
                    displayQuestionsAndAnswers();
                } else {
                    Toast.makeText(ResultsView.this, "error getting results", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QuestionAnswerItem>> call, @NonNull Throwable t) {
                Toast.makeText(ResultsView.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayQuestionsAndAnswers() {
        LinearLayout containerLayout = findViewById(R.id.containerLayout);

        for (QuestionAnswerItem item : questions) {
            LinearLayout questionContainer = new LinearLayout(this);
            questionContainer.setOrientation(LinearLayout.VERTICAL);

            TextView questionTextView = new TextView(this);
            questionTextView.setText("Question: " + item.getQuestionText());

            TextView answerTextView = new TextView(this);
            answerTextView.setText("Answer: " + item.getAnswerText());

            questionContainer.addView(questionTextView);
            questionContainer.addView(answerTextView);

            containerLayout.addView(questionContainer);
        }
    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
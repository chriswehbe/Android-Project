package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.teachersapp.R;
import com.example.teachersapp.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswerAssessment extends AppCompatActivity {

    private RetrofitInterface retrofitInterface;
    private String assessmentID;
    private Retrofit retrofit;
    private List<Question> questions;
    private List<Answer> answers;
    private String userID;
    private MySharedPreference sharedPreference;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_assessment);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        assessmentID = getIntent().getStringExtra("assessmentID");
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        sharedPreference = new MySharedPreference(this);
        userID= sharedPreference.getUserId();
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the logout method
                handleLogout();
            }
        });
        fetchQuestions();

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void fetchQuestions() {
        HashMap<String, String> map = new HashMap<>();
        map.put("assessmentID", assessmentID);
        Call<List<Question>> call = retrofitInterface.getquestions(map);
        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NonNull Call<List<Question>> call, @NonNull Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    questions = response.body();

                    LinearLayout questionContainer = findViewById(R.id.questionContainer);

                    for (int i = 0; i < questions.size(); i++) {
                        Question question = questions.get(i);

                        TextView questionTextView = new TextView(AnswerAssessment.this);
                        questionTextView.setText(question.getText());

                        EditText answerEditText = new EditText(AnswerAssessment.this);
                        answerEditText.setHint("Answer");

                        answerEditText.addTextChangedListener(new AnswerTextWatcher(i));

                        questionContainer.addView(questionTextView);
                        questionContainer.addView(answerEditText);

                        answers.add(new Answer(question.getQuestionid(), ""));
                        Toast.makeText(AnswerAssessment.this, "success", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AnswerAssessment.this, "Error fetching questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Question>> call, @NonNull Throwable t) {
                Toast.makeText(AnswerAssessment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmit() {
        // Check for empty answers
        boolean hasEmptyAnswer = false;

        for (Answer answer : answers) {
            if (answer.getText().trim().isEmpty()) {
                hasEmptyAnswer = true;
                break;
            }
        }

        if (hasEmptyAnswer) {
            Toast.makeText(AnswerAssessment.this, "do not leave empty fields", Toast.LENGTH_SHORT).show();
        }

        HashMap<String, Object> map =new HashMap<>();
        map.put("answers", answers);
        map.put("userid", userID);
        Call<Void> call = retrofitInterface.insertAnswers(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    navigateToUserHome();
                } else {
                    Toast.makeText(AnswerAssessment.this, "Error inserting answers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                Toast.makeText(AnswerAssessment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class AnswerTextWatcher implements TextWatcher {
        private int position;

        public AnswerTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            answers.get(position).setText(editable.toString());
        }
    }
    private void navigateToUserHome(){
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
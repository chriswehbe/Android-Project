package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserHome extends AppCompatActivity {
    private Retrofit retrofit;
    private boolean isAnswered;
    private String userID;
    private RetrofitInterface retrofitInterface;
    private MySharedPreference sharedPreference;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
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
        fetchAssessments();
    }

    private void fetchAssessments() {
        Call<List<AssessmentItem>> call = retrofitInterface.getAssessments();
        call.enqueue(new Callback<List<AssessmentItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<AssessmentItem>> call, @NonNull Response<List<AssessmentItem>> response) {
                if (response.isSuccessful()) {
                    displayAssessments(response.body());
                } else {
                    Toast.makeText(UserHome.this, "Error fetching assessments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AssessmentItem>> call, @NonNull Throwable t) {
                Log.e("AssessmentsView", "Error fetching assessments", t);
                Toast.makeText(UserHome.this, "Error fetching assessments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAssessments(List<AssessmentItem> assessments) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (AssessmentItem item : assessments) {
            if (item.getStatus() == 1) {
                TableRow row = new TableRow(this);

                TextView materialIdTextView = new TextView(this);
                materialIdTextView.setText(String.valueOf(item.getMaterialid()));

                TextView assessmentNameTextView = new TextView(this);
                assessmentNameTextView.setText(item.getAssesmentName());

                TextView yearTextView = new TextView(this);
                yearTextView.setText(String.valueOf(item.getYear()));

                Button actionButton = new Button(this);


                actionButton.setText("Answer");
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("assesmentID", item.getAssesmentid());
                        map.put("userid", userID);
                        Call<IsAnswerd> call = retrofitInterface.isAnswered(map);
                        call.enqueue(new Callback<IsAnswerd>() {
                            @Override
                            public void onResponse(@NonNull Call<IsAnswerd> call, @NonNull Response<IsAnswerd> response) {
                                if (response.isSuccessful()) {
                                    IsAnswerd is=new IsAnswerd();
                                    is = response.body();
                                    if (is.isAnswered()) {
                                        Toast.makeText(UserHome.this, "You already answered this Assessment", Toast.LENGTH_SHORT).show();
                                    }else{
                                        answerAssessment(item.getAssesmentid());
                                    }

                                } else {
                                    Toast.makeText(UserHome.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<IsAnswerd> call, @NonNull Throwable t) {

                                Toast.makeText(UserHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                row.addView(materialIdTextView);
                row.addView(assessmentNameTextView);
                row.addView(yearTextView);
                row.addView(actionButton);

                tableLayout.addView(row);
            }
        }
    }

    private void answerAssessment(String assessmentId) {
        Intent intent = new Intent(this, AnswerAssessment.class);
        intent.putExtra("assessmentID", assessmentId);
        startActivity(intent);

    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
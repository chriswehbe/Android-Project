package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AssessmentView extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_view);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
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
                    Toast.makeText(AssessmentView.this, "Error fetching assessments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AssessmentItem>> call, @NonNull Throwable t) {
                Log.e("AssessmentsView", "Error fetching assessments", t);
                Toast.makeText(AssessmentView.this, "Error fetching assessments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAssessments(List<AssessmentItem> assessments) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (AssessmentItem item : assessments) {
            TableRow row = new TableRow(this);

            TextView materialIdTextView = new TextView(this);
            materialIdTextView.setText(String.valueOf(item.getMaterialid()));

            TextView assessmentNameTextView = new TextView(this);
            assessmentNameTextView.setText(item.getAssesmentName());

            TextView yearTextView = new TextView(this);
            yearTextView.setText(String.valueOf(item.getYear()));

            row.addView(materialIdTextView);
            row.addView(assessmentNameTextView);
            row.addView(yearTextView);



            if (item.getStatus() == 0) {
                Button actionButton = new Button(this);
                actionButton.setText("Activate");
                row.addView(actionButton);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activateAssessment(item.getAssesmentid());
                    }
                });
            } else {
                EditText text = new EditText(this);
                text.setText("Active");
                row.addView(text);
                Button resultButton= new Button(this);
                resultButton.setText("Results");
                row.addView(resultButton);
                resultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(AssessmentView.this, Results.class);
                        intent.putExtra("assessmentID", item.getAssesmentid());
                        startActivity(intent);
                    }
                });
            }




            tableLayout.addView(row);
        }
    }

    private void activateAssessment(String assessmentId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("assesmentid", assessmentId);
        Call<Void> call = retrofitInterface.activateAssessment(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AssessmentView.this, "Assessment activated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(AssessmentView.this, "Error activating assessment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("AssessmentsView", "Error activating assessment", t);
                Toast.makeText(AssessmentView.this, "Error activating assessment", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.teachersapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreCreateAssessment extends AppCompatActivity {

    private String yourValue;
    private String assessmentID;
    private List<QuestionType> questionTypeArray;
    private List<AssessmentType> assessmentTypeArray;
    private String selectedQuestionType;
    private String selectedAssessmentType;
    private String selectedYear;
    private List<String> years;
    private EditText assessmentName;
    private Spinner yearSpinner ;
    private Spinner questionTypeSpinner;
    private Spinner assessmentTypeSpinner;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_create_assessment);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        yourValue = getIntent().getStringExtra("materialID");

        questionTypeArray = new ArrayList<>();
        assessmentTypeArray = new ArrayList<>();
        years = new ArrayList<>();
        years.add("2023/2024");
        years.add("2024/2025");

         yearSpinner = findViewById(R.id.yearSpinner);
         questionTypeSpinner = findViewById(R.id.questionTypeSpinner);
         assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
         assessmentName = findViewById(R.id.assessmentNameEditText);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        ArrayAdapter<QuestionType> questionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypeArray);
        ArrayAdapter<AssessmentType> assessmentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessmentTypeArray);

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);
        questionTypeSpinner.setAdapter(questionTypeAdapter);
        assessmentTypeSpinner.setAdapter(assessmentTypeAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedYear = years.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedYear = "";
            }
        });

        questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedQuestionType = String.valueOf(questionTypeArray.get(position).getQuestionId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedQuestionType = "";
            }
        });

        assessmentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedAssessmentType = String.valueOf(assessmentTypeArray.get(position).getAssessmentTypeId()) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedAssessmentType = "";
            }
        });

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFormSubmit();
            }
        });

        fetchData();
    }

    private void fetchData() {
        Call<List<QuestionType>> questionTypeCall = retrofitInterface.getQuestionTypes();
        questionTypeCall.enqueue(new Callback<List<QuestionType>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuestionType>> call, @NonNull Response<List<QuestionType>> response) {
                questionTypeArray = response.body();
                updateQuestionTypeAdapter(); // Update the adapter
            }

            @Override
            public void onFailure(@NonNull Call<List<QuestionType>> call, @NonNull Throwable t) {
                Toast.makeText(PreCreateAssessment.this, "Error fetching question types", Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<AssessmentType>> assessmentTypeCall = retrofitInterface.getAssessmentTypes();
        assessmentTypeCall.enqueue(new Callback<List<AssessmentType>>() {
            @Override
            public void onResponse(@NonNull Call<List<AssessmentType>> call, @NonNull Response<List<AssessmentType>> response) {
                assessmentTypeArray = response.body();
                updateAssessmentTypeAdapter(); // Update the adapter
            }

            @Override
            public void onFailure(@NonNull Call<List<AssessmentType>> call, @NonNull Throwable t) {
                Toast.makeText(PreCreateAssessment.this, "Error fetching assessment types", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuestionTypeAdapter() {
        ArrayAdapter<QuestionType> questionTypeAdapter = (ArrayAdapter<QuestionType>) questionTypeSpinner.getAdapter();
        if (questionTypeAdapter != null) {
            questionTypeAdapter.clear();
            questionTypeAdapter.addAll(questionTypeArray);
            questionTypeAdapter.notifyDataSetChanged();
        }
    }

    private void updateAssessmentTypeAdapter() {
        ArrayAdapter<AssessmentType> assessmentTypeAdapter = (ArrayAdapter<AssessmentType>) assessmentTypeSpinner.getAdapter();
        if (assessmentTypeAdapter != null) {
            assessmentTypeAdapter.clear();
            assessmentTypeAdapter.addAll(assessmentTypeArray);
            assessmentTypeAdapter.notifyDataSetChanged();
        }
    }

    private void handleFormSubmit() {
        HashMap<String, String> map = new HashMap();
            map.put("materialid", yourValue);
            map.put("assesmentname", assessmentName.getText().toString());
            map.put("assesmenttype", selectedAssessmentType);
            map.put("questiontype", selectedQuestionType);
            map.put("year", selectedYear);
            if(!assessmentName.getText().toString().isEmpty()) {

                Call<Assessment> postAssessment = retrofitInterface.postAssessment(map);
                postAssessment.enqueue(new Callback<Assessment>() {
                    @Override
                    public void onResponse(@NonNull Call<Assessment> call, @NonNull Response<Assessment> response) {
                        if (response.body() != null) {
                            Assessment res = response.body();
                            assessmentID = res.getInsertedId();
                            Toast.makeText(PreCreateAssessment.this, assessmentID, Toast.LENGTH_SHORT).show();
                            navigateToCreateAssessment(assessmentID);

                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<Assessment> call, @NonNull Throwable t) {
                        Toast.makeText(PreCreateAssessment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(PreCreateAssessment.this, "Name should not be empty", Toast.LENGTH_LONG).show();
            }

    }
    private void navigateToCreateAssessment ( String value){
        Intent intent = new Intent(this, CreateAssessment.class);
        intent.putExtra("assessmentID", value);
        startActivity(intent);
        finish();
    }

}
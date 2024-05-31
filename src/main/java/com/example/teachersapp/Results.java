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
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Results extends AppCompatActivity {

    private RetrofitInterface retrofitInterface;
    private List<UserItem> userArray;
    private Retrofit retrofit;
    private MySharedPreference sharedPreference;
    private String BASE_URL = "http://10.0.2.2:8081";
    private String assessmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

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
        }

        fetchUserAnswers();
    }

    private void fetchUserAnswers() {
        HashMap<String, String> map = new HashMap<>();
        map.put("assesmentID", assessmentID);
        Call<List<UserItem>> call = retrofitInterface.getUserAnswers(map);
        call.enqueue(new Callback<List<UserItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserItem>> call, @NonNull Response<List<UserItem>> response) {
                if (response.isSuccessful()) {
                    userArray = response.body();
                    displayUserAnswers();
                    Toast.makeText(Results.this, "success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Results.this, "error getting results", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserItem>> call, @NonNull Throwable t) {
                Log.e("ResultsActivity", t.getMessage());
                Toast.makeText(Results.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayUserAnswers() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        for (UserItem item : userArray) {
            TableRow row = new TableRow(this);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(item.getFname() + " " + item.getLname());

            Button viewButton = new Button(this);
            viewButton.setText("View");
            viewButton.setOnClickListener(v -> navigateToView(item.getUserid()));

            row.addView(nameTextView);
            row.addView(viewButton);

            tableLayout.addView(row);
        }
    }

    private void navigateToView(String userID) {
         Intent intent = new Intent(this, ResultsView.class);
         intent.putExtra("userID", userID);
         intent.putExtra("assessmentID", assessmentID);
         startActivity(intent);
    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
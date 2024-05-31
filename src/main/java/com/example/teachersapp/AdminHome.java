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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminHome extends AppCompatActivity {

    private List<Material> dataArray;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        setContentView(R.layout.activity_admin_home);

        Button createMaterialButton = findViewById(R.id.createMaterialButton);
        Button viewAssessmentsButton = findViewById(R.id.viewAssessmentsButton);
        TableLayout tableLayout = findViewById(R.id.materialTableLayout);
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the logout method
                handleLogout();
            }
        });

        createMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToCreateMaterial();
            }
        });

        viewAssessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAssessmentView();
            }
        });





        Call<List<Material>> call = retrofitInterface.getMaterials();

        call.enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(@NonNull Call<List<Material>> call, @NonNull Response<List<Material>> response) {
                if (response.isSuccessful()) {
                    dataArray = response.body();
                    if (dataArray != null) {
                        for (Material material : dataArray) {
                            Log.d("MaterialData", "MaterialID: " + material.getMaterialID());
                        }
                        populateTable(tableLayout);
                    } else {
                        Toast.makeText(AdminHome.this, "Data array is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AdminHome.this, "Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Material>> call, @NonNull Throwable t) {
                Toast.makeText(AdminHome.this, "Network request failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Print the stack trace for more details
            }
        });
    }

    private void populateTable(TableLayout tableLayout) {
        TableRow tableHeader = new TableRow(this);

        TextView idTextViewH = new TextView(this);
        idTextViewH.setText("ID             ");
        tableHeader.addView(idTextViewH);

        TextView nameTextViewH = new TextView(this);
        nameTextViewH.setText("Name     ");
        tableHeader.addView(nameTextViewH);

        TextView actionTextView = new TextView(this);
        actionTextView.setText("    Action      ");
        tableHeader.addView(actionTextView);
        tableLayout.addView(tableHeader);

        for (Material item : dataArray) {
            TableRow tableRow = new TableRow(this);

            TextView idTextView = new TextView(this);
            idTextView.setText(String.valueOf(item.getMaterialID()));
            tableRow.addView(idTextView);

            TextView nameTextView = new TextView(this);
            nameTextView.setText(item.getMaterialName());
            tableRow.addView(nameTextView);

            Button createAssessmentButton = new Button(this);
            createAssessmentButton.setText("Create Assessment");
            createAssessmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createAssessment(item.getMaterialID());
                }
            });
            tableRow.addView(createAssessmentButton);


            tableLayout.addView(tableRow);
        }
    }

    private void createAssessment(String materialId) {
        Intent intent = new Intent(this, PreCreateAssessment.class);
        intent.putExtra("materialID", materialId);
        startActivity(intent);
    }

    private void navigateToCreateMaterial() {
        Intent intent = new Intent(this, CreateMaterial.class);
        startActivity(intent);
    }
    private void navigateToAssessmentView() {
        Intent intent = new Intent(this, AssessmentView.class);
        startActivity(intent);
    }
    private void handleLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.example.teachersapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateMaterial extends AppCompatActivity {
    private EditText materialID, materialName;
    private Button saveMaterial, cancel;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:8081";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_material);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        EditText materialID = findViewById(R.id.materialID);
        EditText materialName = findViewById(R.id.materialName);
        Button saveMaterial = findViewById(R.id.save_button);
        Button cancel = findViewById(R.id.cancel_button);

        saveMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                if(!materialID.getText().toString().isEmpty() && !materialName.getText().toString().isEmpty()){
                    map.put("materialID", materialID.getText().toString());
                    map.put("materialName", materialName.getText().toString());
                    createMaterial (map);
                }else{
                    Toast.makeText(CreateMaterial.this, "Fields should not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHome();
            }
        });
    }
private void createMaterial( HashMap<String, String> map){
    Call<Void> call = retrofitInterface.createMaterial(map);
    call.enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {

                Toast.makeText(CreateMaterial.this, "Created successfully.", Toast.LENGTH_LONG).show();
                navigateToHome();

        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Toast.makeText(CreateMaterial.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
}
    private void navigateToHome(){
        Intent intent = new Intent(this, AdminHome.class);
        startActivity(intent);
    }

}
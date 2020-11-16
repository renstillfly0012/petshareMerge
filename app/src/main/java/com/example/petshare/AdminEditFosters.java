package com.example.petshare;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class AdminEditFosters extends AppCompatActivity {

    Button AAsubmit;
    private HashMap<String, String> jsonUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_fosters);

        AAsubmit = findViewById(R.id.popup_admin_edit_foster_btnSubmit);

        final OkHttpClient client = new OkHttpClient();
        final String URL = Url.fosterurl;
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    jsonUserData = new HashMap<String,String>();
                    try {
                        jsonUserData.put("id",new JSONObject(myResponse).getString("id"));
                        jsonUserData.put("name",new JSONObject(myResponse).getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Intent intent = new Intent(getBaseContext(),ViewFosters.class);
        intent.putExtra("KEY_ID",jsonUserData.get("id"));
        intent.putExtra("KEY_NAME",jsonUserData.get("name"));
        startActivity(intent);
    }
}
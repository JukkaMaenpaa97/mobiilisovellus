package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JobOrder extends AppCompatActivity {


    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_order);
        Intent intent = getIntent();

        mQueue = Volley.newRequestQueue(this);
        jsonParse();

    }

    private void jsonParse() {

    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), CustomerProfile.class);
        startActivity(intent);
    }

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void buttonJobClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), JobInfo.class);
        startActivity(intent);
    }
}
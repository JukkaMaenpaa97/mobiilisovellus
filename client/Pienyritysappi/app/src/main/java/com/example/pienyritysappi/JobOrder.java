package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private Button mButton1;
    private RequestQueue mQueue;
    private String url;
    private int jobCount=1;
    private String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_order);
        Intent intent = getIntent();
        user_id = getIntent().getStringExtra("keyuser_id");
        url = "http://mobiilisovellus.therozor.com:5000/services?user_id="+user_id;
        System.out.println("JobOrder url: "+ url);
        mButton1 = findViewById(R.id.button22);
        mQueue = Volley.newRequestQueue(this);

        jsonParseJobCount();
        for(int i = 0; i<jobCount; i++) {
            jsonParseButtons(i);
        }

    }

    private void jsonParseJobCount() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jobCount = response.getInt("count");


                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan.");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void jsonParseButtons(int i) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject job = jsonArray.getJSONObject(i);   //tähän indeksi, monennenko työn haluaa (0=eka 1=toka jne. tullaan vaihtamaan id:llä haettavaksi)
                            String jobTitle = job.getString("service_title");
                            String jobDescription = job.getString("service_description");
                            String jobAvailability = job.getString("service_availability");
                            switch(i) {
                                case 0:
                                    mButton1.setText(jobTitle + "\n" + jobDescription + "\nSaatavuus: " + jobAvailability);
                                    break;
                                case 1:
                                    mButton1.setText(jobTitle + "\n" + jobDescription + "\nSaatavuus: " + jobAvailability);
                                    break;
                                case 2:
                                    mButton1.setText(jobTitle + "\n" + jobDescription + "\nSaatavuus: " + jobAvailability);
                                    break;

                            }
                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa siis\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan.");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
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
        intent.putExtra("keyurl",url);
        startActivity(intent);
    }
}
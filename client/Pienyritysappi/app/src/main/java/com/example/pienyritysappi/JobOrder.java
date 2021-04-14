package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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
    private String url;
    private String user_id = "";
    private Button nButton;
    private JSONArray jsonArray;
    private JSONObject job;
    private int jobCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_order);
        Intent intent = getIntent();
        user_id = getIntent().getStringExtra("user_id");
        url = "http://mobiilisovellus.therozor.com:5000/services?user_id="+user_id;
        System.out.println("JobOrder url: "+ url);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();

    }

    private void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            jobCount = response.getInt("count");

                            for(int i = 0; i< jobCount; i++) {
                                addButton(i); //luo myös onClickListenerin
                            }

                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan. Category.java");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void addButton(int i) {
        GridLayout layout = (GridLayout)findViewById(R.id.jobButtonGridLayout);
        nButton = new Button(this);
        try {
            job = jsonArray.getJSONObject(i);
            String jobName = job.getString("service_title");
            String jobAvailability = job.getString("service_availability");
            String jobPrice = job.getString("service_price");
            String jobId = job.getString("service_id");
            String buttonText = jobName + "\nSaatavilla: " + jobAvailability + "\n" + jobPrice + " €";

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(10,10,10,10);
            nButton.setText(buttonText);
            nButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nButton.setLayoutParams(params);
            nButton.setBackground(nButton.getContext().getDrawable(R.drawable.rounded_button));
            nButton.setPadding(30,0,30,0);
            String jobBaseUrl = "http://mobiilisovellus.therozor.com:5000/service/";
            String joburl = jobBaseUrl + jobId;
            nButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),JobInfo.class);
                    intent.putExtra("keyurl", joburl);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layout.addView(nButton);
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
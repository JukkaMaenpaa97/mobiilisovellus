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

    private Button mButton;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_order);
        Intent intent = getIntent();
        mButton = findViewById(R.id.button22);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();

    }

    private void jsonParse() {
        String url = "http://mobiilisovellus.therozor.com:5000/services?user_id=5031bd69-c634-43dd-9000-c8fe0b984e85";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject job = jsonArray.getJSONObject(0);   //tähän indeksi, monennenko työn haluaa (0=eka 1=toka jne. tullaan vaihtamaan id:llä haettavaksi)
                            String jobTitle = job.getString("service_title");
                            String jobDescription = job.getString("service_description");
                            String jobAvailability = job.getString("service_availability");
                            mButton.setText(jobTitle + "\n" + jobDescription + "\nSaatavuus: " + jobAvailability);

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
        String url = "http://mobiilisovellus.therozor.com:5000/services?user_id=5031bd69-c634-43dd-9000-c8fe0b984e85";
        intent.putExtra("keyurl",url);
        startActivity(intent);
    }
}
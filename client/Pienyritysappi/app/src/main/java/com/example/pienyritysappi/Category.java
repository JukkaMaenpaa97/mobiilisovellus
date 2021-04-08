package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class Category extends AppCompatActivity {

    private TextView mTextViewCategoryName;
    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private RequestQueue mQueue;
    String categoryurl;
    private int serviceCount=1;
    private String companyUserId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        categoryurl = "http://mobiilisovellus.therozor.com:5000/providers?category_id=6a808015-417c-4bea-8b44-1b9be714bea1";//getIntent().getStringExtra("keyurl");
        mButton1 = findViewById(R.id.button12);
        mButton2 = findViewById(R.id.button19);
        mButton3 = findViewById(R.id.button20);
        mButton4 = findViewById(R.id.button21);
        mTextViewCategoryName = findViewById(R.id.textViewCategoryName);
        mQueue = Volley.newRequestQueue(this);
        jsonParseCompanyName();
        jsonParseCompanyCount();
        for(int i = 0; i<=serviceCount; i++) {
            jsonParseButtons(i);
        }


    }

    private void jsonParseCompanyName() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, categoryurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mTextViewCategoryName.setText(response.getString("category_name"));


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

    private void jsonParseCompanyCount() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, categoryurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            serviceCount = response.getInt("count");


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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, categoryurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject category = jsonArray.getJSONObject(i);   //tähän indeksi monennenko haluaa (0=eka 1=toka jne. tullaan vaihtamaan id:llä haettavaksi)
                            String companyName = category.getString("user_company_name");
                            String contactName = category.getString("user_name");
                            switch (i){
                                case 0:
                                    mButton1.setText(companyName + "\nYhteyshenkilö: " + contactName);
                                    break;
                                case 1:
                                    mButton2.setText(companyName + "\nYhteyshenkilö: " + contactName);
                                    break;
                                case 2:
                                    mButton3.setText(companyName + "\nYhteyshenkilö: " + contactName);
                                    break;
                                case 3:
                                    mButton4.setText(companyName + "\nYhteyshenkilö: " + contactName);
                                    break;

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

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void buttonCompanyClicked(View view) {
        jsonParseUserId();
        Intent intent = new Intent(getApplicationContext(),Company.class);
        intent.putExtra("userId", companyUserId);
        startActivity(intent);

    }

    private void jsonParseUserId() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, categoryurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject category = jsonArray.getJSONObject(0);   //tähän indeksi monennenko haluaa (0=eka 1=toka jne. tullaan vaihtamaan id:llä haettavaksi)
                            companyUserId = category.getString("user_id");

                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan.Category.java jsonParseUserId");
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

}
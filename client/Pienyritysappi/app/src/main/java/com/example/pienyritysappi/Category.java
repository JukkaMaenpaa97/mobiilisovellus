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

public class Category extends AppCompatActivity {

    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        mTextViewResult = findViewById(R.id.textViewCategoryName);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse(){
        String url = "http://mobiilisovellus.therozor.com:5000/categories";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject category = jsonArray.getJSONObject(i);
                                System.out.println("\n" + category + "\n");
                                String categoryName = category.getString("category_name");
                                System.out.println("\n" + categoryName + "\n");
                                mTextViewResult.setText(categoryName);
                            }

                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä\n");
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

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void companyImageClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),Company.class);
        startActivity(intent);
    }

    public void buttonCompanyClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),Company.class);
        startActivity(intent);
    }

}
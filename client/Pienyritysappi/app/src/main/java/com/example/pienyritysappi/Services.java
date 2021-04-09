package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class Services extends AppCompatActivity {

    private Button button;
    private RequestQueue mQueue;
    private String catUrl;
    private int categoryCount = 1;
    private String categoryName;
    private JSONArray jsonArray;
    private JSONObject category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Intent intent = getIntent();
        catUrl = "http://mobiilisovellus.therozor.com:5000/categories";
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void addButton(int i)
    {
        GridLayout gridLayout = (GridLayout)findViewById(R.id.categoryGridLayout);
        button = new Button(this);
        try{
            category = jsonArray.getJSONObject(i);
            categoryName = category.getString("category_name");
            String buttonText = categoryName ;
            String categoryId = category.getString("category_id");
            button.setText(buttonText);
            button.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            button.setBackgroundColor(Color.GREEN);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Category.class);
                    intent.putExtra("categoryId", categoryId);
                    startActivity(intent);
                }
            });
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        gridLayout.addView(button);
    }

    private void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, catUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            categoryCount = response.getInt("count");
                            for(int i = 0; i<categoryCount; i++)
                            {
                                addButton(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void profileButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void homeButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
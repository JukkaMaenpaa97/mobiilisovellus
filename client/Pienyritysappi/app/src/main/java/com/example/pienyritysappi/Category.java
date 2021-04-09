package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Category extends AppCompatActivity {

    private TextView mTextViewCategoryName;
    private RequestQueue mQueue;
    private String categoryurl;
    private int companyCount =1;
    private Button nButton;
    private String companyName;
    private String contactName;
    private JSONArray jsonArray;
    private JSONObject company;
    private String baseUrl = "http://mobiilisovellus.therozor.com:5000/providers?category_id=";
    private String categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryurl = baseUrl + categoryId;
        mTextViewCategoryName = findViewById(R.id.textViewCategoryName);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, categoryurl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            companyCount = response.getInt("count");
                            mTextViewCategoryName.setText(response.getString("category_name"));
                            for(int i = 0; i< companyCount; i++) {
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
        GridLayout layout = (GridLayout)findViewById(R.id.CompanyButtonGridLayout);
        nButton = new Button(this);
        try {
            company = jsonArray.getJSONObject(i);
            companyName = company.getString("user_company_name");
            contactName = company.getString("user_name");
            String buttonText = companyName + "\nYhteyshenkilö: " + contactName;
            String companyUserId = company.getString("user_id");
            nButton.setText(buttonText);
            nButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),Company.class);
                    intent.putExtra("userId", companyUserId);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layout.addView(nButton);
    }

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }
}
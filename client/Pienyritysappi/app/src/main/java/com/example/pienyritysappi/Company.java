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

public class Company extends AppCompatActivity {

    private String user_id = "";
    private String baseurl = "http://mobiilisovellus.therozor.com:5000/user/";
    private String url = "";
    private RequestQueue mQueue;
    private TextView mTextViewCompanyName;
    private TextView mTextViewCompanyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        Intent intent = getIntent();
        mTextViewCompanyName = findViewById(R.id.companyName);
        mTextViewCompanyInfo = findViewById(R.id.companyInfoTextView);
        Bundle extras = intent.getExtras();
        user_id = intent.getStringExtra("userId");
        System.out.println(user_id);
        url = baseurl + user_id;
        System.out.println(url);
        mQueue = Volley.newRequestQueue(this);

        jsonParseCompanyNameAndInfo();

    }

    private void jsonParseCompanyNameAndInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject company = jsonArray.getJSONObject(0);
                            mTextViewCompanyName.setText(company.getString("user_company_name"));
                            String companyUser = company.getString("user_name");
                            String companyId = company.getString("user_company_id");
                            String companyInfo = "Yhteyshenkilö: " + companyUser + "\n" + "Y-tunnus: " + companyId;
                            mTextViewCompanyInfo.setText(companyInfo);


                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan. Company.java jsonParseCompanyName");
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

    public void contactButtonClicked(View view)
    {
        System.out.println(url);
    }

    public void jobListingsButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CompanyServices.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}
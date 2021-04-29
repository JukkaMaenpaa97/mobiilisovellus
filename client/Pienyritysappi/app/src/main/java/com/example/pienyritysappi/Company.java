package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mCompanyAddressTextView;
    private TextView mCompanyPhoneTextView;
    private TextView mCompanyContactTextView;
    private TextView mCompanyIdTextView;
    Globals g = Globals.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        Intent intent = getIntent();
        mTextViewCompanyName = findViewById(R.id.companyName);
        mCompanyAddressTextView = findViewById(R.id.companyAddress2TextView);
        mCompanyContactTextView = findViewById(R.id.companyContact2TextView);
        mCompanyPhoneTextView = findViewById(R.id.companyPhone2TextView);
        mCompanyIdTextView = findViewById(R.id.companyYtunnus2TextView);

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
                            String postal = company.getString("user_postalcode");
                            String address = company.getString("user_address");
                            String city = company.getString("user_city");
                            String companyContact = address + "," + postal + "," + city;
                            mCompanyAddressTextView.setText(companyContact);
                            String companyUser = company.getString("user_name");
                            mCompanyContactTextView.setText(companyUser);
                            String phone = company.getString("user_phone");
                            mCompanyPhoneTextView.setText(phone);
                            String companyId = company.getString("user_company_id");
                            mCompanyIdTextView.setText(companyId);




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
        int userType = g.getUser_type();

        if(userType == 1) {
            Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
            startActivity(intent);
        }
        else if(userType == 2) {
            Intent intent = new Intent(getApplicationContext(),CompanyEditProfile.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(),"Et ole kirjautuneena sisään",Toast.LENGTH_SHORT).show();
        }
    }

    public void homeButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void jobListingsButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CompanyServices.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}
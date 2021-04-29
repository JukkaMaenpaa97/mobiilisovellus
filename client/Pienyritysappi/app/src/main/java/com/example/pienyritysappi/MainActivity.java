package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Globals g = Globals.getInstance();
    private String logInUrl = "http://mobiilisovellus.therozor.com:5000/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button1Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), ChosenCategory.class);
        startActivity(intent);
    }
    public void button2Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Company.class);
        startActivity(intent);
    }
    public void button3Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), CompanyServices.class);
        startActivity(intent);
    }
    public void button4Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }
    public void button5Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Categories.class);
        startActivity(intent);
    }

    public void button6Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),JobInfo.class);
        startActivity(intent);
    }

    public void reserveButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),ReservationActivity.class);
        startActivity(intent);
    }

    public void button10Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),LogIn.class);
        startActivity(intent);
    }

    public void button7Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void addJobClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), AddJob.class);
        startActivity(intent);
    }

    public void companyEditProfileClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),CompanyEditProfile.class);
        startActivity(intent);
    }

    public void orderedServicesClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),OrderedServices.class);
        startActivity(intent);
    }

    public void LogOutClicked(View view) {
        String apikey = g.getApi_key();
        System.out.println(apikey);
        g.setApi_key("");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, logInUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        String apikey = g.getApi_key();
                        if (apikey == "") {
                            Toast.makeText(getApplicationContext(), "Uloskirjautuminen onnistui", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", apikey);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
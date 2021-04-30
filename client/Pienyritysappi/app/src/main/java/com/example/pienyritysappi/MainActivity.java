package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        String userid = g.getUser_id();
        Button logButton = findViewById(R.id.buttonLogOut);
        if (userid == null){
            logButton.setText("Kirjaudu sisään");
        }else{
            logButton.setText("Kirjaudu ulos");
        }
    }

    public void button5Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Categories.class);
        startActivity(intent);
    }

    public void button7Clicked(View view)
    {
        int userType = g.getUser_type();
        if (userType == 1) {
            Intent intent = new Intent(getApplicationContext(), CustomerProfile.class);
            startActivity(intent);
        }else if (userType == 2){
            Intent intent = new Intent(getApplicationContext(), CompanyEditProfile.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void addJobClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), AddJob.class);
        startActivity(intent);
    }

    public void companyProfileClicked(View view)
    {
        int userType = g.getUser_type();
        if (userType == 1) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if (userType == 2){
            String userID = g.getUser_id();
            Intent intent = new Intent(getApplicationContext(), Company.class);
            intent.putExtra("userId", userID);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void orderedServicesClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),OrderedServices.class);
        startActivity(intent);
    }

    public void LogInOutClicked(View view) {
        String apikey = g.getApi_key();
        System.out.println(apikey);
        if (apikey == null){
            Intent intent = new Intent(getApplicationContext(),LogIn.class);
            startActivity(intent);
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, logInUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        g.setApi_key(null);
                        String apikey = g.getApi_key();
                        if (apikey == null) {
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
        g.setApi_key(null);
        g.setUser_id(null);
        g.setUser_type(0);
        Button logButton = findViewById(R.id.buttonLogOut);
        logButton.setText("Kirjaudu sisään");
        requestQueue.add(jsonObjectRequest);
    }
}
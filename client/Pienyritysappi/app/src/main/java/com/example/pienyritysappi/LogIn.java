package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogIn extends AppCompatActivity {

    private String logInUrl = "http://mobiilisovellus.therozor.com:5000/login";
    private String logInEmail = "";
    private String logInPassword = "";
    private EditText etEmail;
    private EditText etPassword;
    private String apikey = "";
    private String userId = "";
    private String registeredPassword = "";
    private String registeredEmail = "";
    Globals g = Globals.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        try {
            Intent intent = getIntent();
            registeredEmail = intent.getStringExtra("registered_email");
            registeredPassword = intent.getStringExtra("registered_password");
            etEmail.setText(registeredEmail);
            etPassword.setText(registeredPassword);
        }catch (Exception e){
            System.out.println("ei askeista rekisterointia");
        }
    }

    public void logInButtonClicked(View view) {
        Toast.makeText(getApplicationContext(), "tässä saattaa kestää hetki", Toast.LENGTH_SHORT).show();
        logInEmail = etEmail.getText().toString();
        logInPassword = etPassword.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("user_email", logInEmail);
            postData.put("user_password", logInPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, logInUrl, postData,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                try {
                    apikey = response.getString("apikey");
                    System.out.println(apikey);
                    System.out.println(userId);
                    g.setApi_key(apikey);
                    String apikeyglobalista=g.getApi_key();
                    System.out.println("apikey globalista: " + apikeyglobalista);
                    g.setUser_type(response.getInt("user_type"));
                    //tähän väliin apikkeyllä pyyntö user/me osoitteeseen josta tallennetaan käyttäjän tiedot

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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
        requestQueue.add(jsonObjectRequest);
    }

    public void registerCustomerButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }


    public void logInLessClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), Categories.class);
        startActivity(intent);
    }
}
package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmReservation extends AppCompatActivity {

    private RequestQueue mQueue;
    private JSONArray jsonArray;
    private JSONObject order;
    private int index = 0;
    private TextView tvHeader;
    private TextView tvInfo;
    private String infotext;
    private String price;
    private String description;
    private String availability;
    private String headertext;
    private String url;
    private String postUrl = "http://mobiilisovellus.therozor.com:5000/orders";
    private String serviceId;
    private String orderComments;
    private EditText mEditText;
    private JSONObject postData;
    private String api_key;
    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);
        api_key = g.getApi_key();
        if (api_key == ""){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            Toast.makeText(getApplicationContext(),"Et ole kirjautunut tai kirjautumisesi on vanhentunut",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        try{
            url = getIntent().getStringExtra("url");}
        catch (Exception e){
            System.out.println("ei urlia");
        }
        System.out.println(index);
        mEditText = findViewById(R.id.editTextjobComments);
        tvHeader = findViewById(R.id.textViewConfirmHeader);
        tvInfo = findViewById(R.id.textViewConfirmInfo);
        mQueue = Volley.newRequestQueue(this);
        getServiceInfo();
    }

    private void getServiceInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            jsonArray = response.getJSONArray("data");
                            order = jsonArray.getJSONObject(0);
                            serviceId = order.getString("service_id");
                            headertext = order.getString("service_title");
                            description = order.getString("service_description");
                            price = order.getString("service_price");
                            availability = order.getString("service_availability");
                            infotext = description + "\nSaatavuus: " + availability + "\nHinta: " + price + " €";
                            tvHeader.setText(headertext);
                            tvInfo.setText(infotext);

                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan. ConfirmReservation.java");
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

    public void ConfirmClicked(View view) {
        System.out.println(url);
        orderComments = mEditText.getText().toString();
        System.out.println(orderComments);
        String usernameInput = mEditText.getText().toString().trim();
        if (usernameInput.length() > 75) {
            mEditText.setError("kommentti liian pitkä");
        }else{
                postNewOrder();
            }
    }

    private void postNewOrder() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postData = new JSONObject();
        try {
            postData.put("order_service_id", serviceId);
            postData.put("order_comments", orderComments);
            System.out.println("postData.put shitit laitettu");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                System.out.println("onnistui");
                Intent intent = new Intent(getApplicationContext(), OrderedServices.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("errorResponse");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", api_key);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
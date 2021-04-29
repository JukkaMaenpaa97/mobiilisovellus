package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ChosenOrder extends AppCompatActivity {


    private String baseurl = "http://mobiilisovellus.therozor.com:5000/order/";
    private String orderID = "";
    private String url = "";
    private String api_key;
    private RequestQueue mQueue;
    private TextView tvOrderInfo;
    private TextView tvOrderTitle;
    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_order);
        api_key = g.getApi_key();
        if (api_key == ""){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            Toast.makeText(getApplicationContext(),"Et ole kirjautunut sisään tai kirjautumisesi on vanhentunut",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
        orderID = getIntent().getStringExtra("orderID");
        url = baseurl + orderID;
        tvOrderInfo = findViewById(R.id.textViewOrderInfo);
        tvOrderTitle = findViewById(R.id.textViewOrderTitle);
        System.out.println(orderID);
        System.out.println(url);
        mQueue = Volley.newRequestQueue(this);
        getOrderInfo();
    }

    private void getOrderInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject order = jsonArray.getJSONObject(0);
                            JSONObject service = order.getJSONObject("order_service_info");
                            String serviceName = service.getString("service_title");
                            Double orderPrice = service.getDouble("service_price");
                            String jobDescription = service.getString("service_description");
                            String orderComment = order.getString("order_comments");
                            tvOrderTitle.setText(serviceName);
                            int servicePriceType = service.getInt("service_type");
                            String servicepricetypeString;
                            if (servicePriceType==1){
                                servicepricetypeString = "/tunti";
                            }else{
                                servicepricetypeString = " kertamaksu";
                            }
                            String orderInfo = jobDescription + "\nhinta: " + orderPrice + " €" + servicepricetypeString + "\nkommentti: " + orderComment;
                            tvOrderInfo.setText(orderInfo);
                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONException\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan.");
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", api_key);
                return headers;
            }
        };
        mQueue.add(request);
    }

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
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

    public void goBackButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),OrderedServices.class);
        startActivity(intent);
    }
}
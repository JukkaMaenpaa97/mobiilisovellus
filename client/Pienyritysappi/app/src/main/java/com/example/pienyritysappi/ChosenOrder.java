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

public class ChosenOrder extends AppCompatActivity {


    private String baseurl = "http://mobiilisovellus.therozor.com:5000/order/";
    private String orderID = "";
    private String url = "";
    private RequestQueue mQueue;
    private TextView tvOrderInfo;
    private TextView tvOrderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_order);
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
                            //int orderCreatedTime = order.getInt("order_created"); // älä vittu edes yritä uncommenttaa tätä
                            String orderComment = order.getString("order_comments");
                            tvOrderTitle.setText(serviceName);
                            int servicePriceType = service.getInt("service_Type");
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
}
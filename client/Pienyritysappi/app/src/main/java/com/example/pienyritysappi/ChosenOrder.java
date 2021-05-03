package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChosenOrder extends AppCompatActivity {


    private String baseurl = "http://mobiilisovellus.therozor.com:5000/order/";
    private String orderID = "";
    private String url = "";
    private String api_key;
    private String orderComment;
    private int userType;
    private int orderstatus;
    private RequestQueue mQueue;
    private TextView tvOrderInfo;
    private TextView tvTila;
    private TextView tvOrderTitle;
    private Spinner orderStatusSpinner;
    private Button updateButton;
    private ArrayAdapter<String> spinnerAdapter;
    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_order);
        api_key = g.getApi_key();
        userType = g.getUser_type();
        updateButton = findViewById(R.id.button);
        orderStatusSpinner = findViewById(R.id.spinnerOrderStatus);
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatusSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter.add("Odottaa");
        spinnerAdapter.add("Hyväksytty");
        spinnerAdapter.add("Tehty");
        spinnerAdapter.add("Peruuttettu");
        spinnerAdapter.notifyDataSetChanged();
        orderStatusSpinner.setAdapter(spinnerAdapter);
        if (api_key == null){
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
                            JSONObject senderInfo = order.getJSONObject("order_sender_info");
                            String ordererName = senderInfo.getString("user_name");
                            JSONObject service = order.getJSONObject("order_service_info");
                            String serviceName = service.getString("service_title");
                            Double orderPrice = service.getDouble("service_price");
                            String jobDescription = service.getString("service_description");
                            orderComment = order.getString("order_comments");
                            tvOrderTitle.setText(serviceName);
                            int servicePriceType = service.getInt("service_type");
                            orderstatus = order.getInt("order_status");
                            orderstatus--;
                            String status = "";
                            switch (orderstatus){
                                case 0:
                                    status="Odottaa";
                                    break;
                                case 1:
                                    status="Hyväksytty";
                                    break;
                                case 2:
                                    status="Tehty";
                                    break;
                                case 3:
                                    status="Peruutettu";
                                    break;
                            }
                            String servicepricetypeString;
                            if (servicePriceType==1){
                                servicepricetypeString = "/tunti";
                            }else{
                                servicepricetypeString = " kertamaksu";
                            }
                            String orderInfo = jobDescription + "\nhinta: " + orderPrice + " €" + servicepricetypeString + "\nTilaaja: " + ordererName + "\nkommentti: " + orderComment + "\ntila: " + status;
                            tvOrderInfo.setText(orderInfo);
                            orderStatusSpinner.setSelection(orderstatus);
                            if (userType == 1){
                                updateButton.setVisibility(View.INVISIBLE);
                                orderStatusSpinner.setVisibility(View.INVISIBLE);
                                tvTila = findViewById(R.id.textView13);
                                tvTila.setVisibility(View.INVISIBLE);
                            }else{
                                updateButton.setVisibility(View.VISIBLE);
                                orderStatusSpinner.setVisibility(View.VISIBLE);
                            }
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

    public void updateClicked(View view) {
        if (userType == 2){
            orderstatus = orderStatusSpinner.getSelectedItemPosition();
            System.out.println("orderstatus: " + orderstatus);
            JSONObject postData = new JSONObject();
            try {
                postData.put("order_id",orderID);
                postData.put("order_status", orderstatus+1);
                postData.put("order_comments", orderComment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, postData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(),"tilaus päivitetty!",Toast.LENGTH_SHORT).show();
                            System.out.println("tilaus päivitetty!");
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
    }
}
package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private String serviceId;
    private String orderComments;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);
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
                System.out.println("onErrorResponsessa ollaan. Category.java");
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

    public void ConfirmClicked(View view) {
        System.out.println(url);
        orderComments = mEditText.getText().toString();
        System.out.println(orderComments);
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }
}
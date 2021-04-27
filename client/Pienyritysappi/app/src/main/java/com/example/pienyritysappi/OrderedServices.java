package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

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

public class OrderedServices extends AppCompatActivity {

    private String Url = "http://mobiilisovellus.therozor.com:5000/orders";
    private String serviceTitle;
    private String orderPrice;
    private int orderStatus;
    private String orderStatusString;
    private String serviceProvider;
    private String buttonText;
    private String api_key = "A5NG1QCBjxNwikVq2zocyAOtGXw3oZCm";
    private Button nButton;
    private RequestQueue mQueue;
    private int orderCount;
    private JSONArray jsonArray;
    private JSONObject service;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_services);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {
        request = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            orderCount = response.getInt("count");
                            for(int i = 0; i< orderCount; i++) {
                                addButton(i);
                            }
                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa\n");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("onErrorResponsessa ollaan");
                error.printStackTrace();
                GridLayout layout = (GridLayout)findViewById(R.id.OrderedServicesGridLayout);
                nButton = new Button(getApplicationContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(10,10,10,10);
                nButton.setLayoutParams(params);
                nButton.setBackground(nButton.getContext().getDrawable(R.drawable.rounded_button));
                nButton.setText("ei tilauksia");
                nButton.setPadding(30,0,30,0);
                nButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                layout.addView(nButton);
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

    private void addButton(int i) {
        GridLayout layout = (GridLayout)findViewById(R.id.OrderedServicesGridLayout);
        nButton = new Button(this);
        try {
            service = jsonArray.getJSONObject(i);
            //tässä kohtaa tarvii saada tolla i-indeksillä sen kyseisen servicen tiedot; palvelunimi, palvelu selostus, mahdollinen kommentti, palvelun tila, yms. ja laittaa ne buttonTextiin
            serviceTitle = service.getString("service_title");
            orderPrice = service.getString("service_price");
            serviceProvider = service.getString("service_provider_name");
            orderStatus = service.getInt("order_status");
            switch(orderStatus){
                case 1:
                    orderStatusString="Odottaa";
                    break;
                case 2:
                    orderStatusString="Hyväksytty";
                    break;
                case 3:
                    orderStatusString="Tehty";
                    break;
                case 4:
                    orderStatusString="Peruutettu";
                    break;
            }
            buttonText = serviceTitle + "\ntila: " + orderStatusString + "\n" + serviceProvider + "\nhinta: " + orderPrice + " €";
            //mieti myös onClickiä
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(10,10,10,10);
            nButton.setLayoutParams(params);
            nButton.setBackground(nButton.getContext().getDrawable(R.drawable.rounded_button));
            nButton.setText(buttonText);
            nButton.setPadding(30,0,30,0);
            nButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layout.addView(nButton);
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
package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

public class AddJob extends AppCompatActivity {

    private RequestQueue mQueue;
    private Spinner mSpinner;
    private int categoryCount;
    private JSONArray jsonArray;
    private JSONObject category;
    private String catUrl = "http://mobiilisovellus.therozor.com:5000/categories";
    private String addJobUrl = "http://mobiilisovellus.therozor.com:5000/services";
    private String categoryName;
    private ArrayAdapter<String> spinnerAdapter;
    private TextView tvJobTitle;
    private TextView tvJobDescription;
    private TextView tvJobPriceType;
    private TextView tvJobPrice;
    private TextView tvJobAvailability;
    private String job_title;
    private String job_description;
    private String job_price_type;
    private String job_price;
    private String job_availability;
    private String job_category;
    private TextView apikey;
    private String api_key;
    private int spinnerposition;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        mSpinner = findViewById(R.id.spinner);
        tvJobTitle = findViewById(R.id.editTextJobName);
        tvJobDescription = findViewById(R.id.editTextJobDescription);
        tvJobPriceType = findViewById(R.id.editTextPriceType);
        tvJobPrice = findViewById(R.id.editTextPrice);
        tvJobAvailability = findViewById(R.id.editTextJobAvailability);
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mQueue = Volley.newRequestQueue(this);
        parseCategories();
        System.out.println("parsen jälkeen on pouta sää");
    }

    private void parseCategories() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, catUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonArray = response.getJSONArray("data");
                            categoryCount = response.getInt("count");
                            for(int i = 0; i < categoryCount; i++){
                                category = jsonArray.getJSONObject(i);
                                categoryName = category.getString("category_name");
                                spinnerAdapter.add(categoryName);
                                spinnerAdapter.notifyDataSetChanged();
                                mSpinner.setSelection(i);
                            }
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
        mQueue.add(request);
    }

    public void addJobConfirmClicked(View view) {
        job_availability = tvJobAvailability.getText().toString();
        job_description = tvJobDescription.getText().toString();
        spinnerposition = mSpinner.getSelectedItemPosition();
        try {
            category = jsonArray.getJSONObject(spinnerposition);
            categoryId = category.getString("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        job_price = tvJobPrice.getText().toString();
        job_price_type = tvJobPriceType.getText().toString();
        job_title = tvJobTitle.getText().toString();
        apikey = (TextView)findViewById(R.id.editTextApikey);
        api_key = "A5NG1QCBjxNwikVq2zocyAOtGXw3oZCm";
        System.out.println(job_availability);
        System.out.println(categoryId);
        System.out.println(job_description);
        System.out.println(job_price);
        System.out.println(job_price_type);
        System.out.println(job_title);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("service_category", categoryId);
            postData.put("service_type", 1);
            postData.put("service_title", job_title);
            postData.put("service_description", job_description);
            postData.put("service_price_type", job_price_type);
            postData.put("service_price", job_price);
            postData.put("service_availability", job_availability);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addJobUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Intent intent = new Intent(getApplicationContext(), Categories.class);
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

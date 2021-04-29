package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.HashMap;
import java.util.Map;

public class AddJob extends AppCompatActivity {

    private RequestQueue mQueue;
    private Spinner mSpinner;
    private Spinner mSpinnerPriceType;
    private int categoryCount;
    private JSONArray jsonArray;
    private JSONObject category;
    private String catUrl = "http://mobiilisovellus.therozor.com:5000/categories";
    private String addJobUrl = "http://mobiilisovellus.therozor.com:5000/services";
    private String categoryName;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> spinnerAdapterPriceType;
    private TextView tvJobTitle;
    private TextView tvJobDescription;
    private TextView tvJobPrice;
    private TextView tvJobAvailability;
    private String job_title;
    private String job_description;
    private int job_price_type;
    private String job_price;
    private String job_availability;
    private String api_key;
    private int spinnerposition;
    private int spinnerpositionpricetype;
    private String categoryId;
    private JSONObject postData;
    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        api_key = g.getApi_key();
        int usertype = g.getUser_type();
        if (api_key == null){
            Toast.makeText(getApplicationContext(),"Kirjautumisesi on vanhentunut",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else if(usertype != 2){
            Toast.makeText(getApplicationContext(),"Et ole kirjautuneena yritystilillä",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        mSpinner = findViewById(R.id.spinner);
        mSpinnerPriceType = findViewById(R.id.spinnerpricetype);
        tvJobTitle = findViewById(R.id.editTextJobName);
        tvJobDescription = findViewById(R.id.editTextJobDescription);
        tvJobPrice = findViewById(R.id.editTextPrice);
        tvJobAvailability = findViewById(R.id.editTextJobAvailability);
        spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        spinnerAdapterPriceType = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapterPriceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterPriceType.add("Kertamaksu");
        spinnerAdapterPriceType.add("Tuntihinta");
        spinnerAdapterPriceType.notifyDataSetChanged();
        mSpinnerPriceType.setAdapter(spinnerAdapterPriceType);
        mQueue = Volley.newRequestQueue(this);
        getCategories();
    }

    private void getCategories() {
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
        spinnerpositionpricetype = mSpinnerPriceType.getSelectedItemPosition() + 1;
        try {
            category = jsonArray.getJSONObject(spinnerposition);
            categoryId = category.getString("category_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        job_price = tvJobPrice.getText().toString();
        job_price_type = 1;
        job_title = tvJobTitle.getText().toString();

        System.out.println(job_availability);
        System.out.println(categoryId);
        System.out.println(job_description);
        System.out.println(job_price);
        System.out.println(job_price_type);
        System.out.println(job_title);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postData = new JSONObject();
        try {
            postData.put("service_category", categoryId);
            postData.put("service_type", job_price_type);
            postData.put("service_title", job_title);
            postData.put("service_description", job_description);
            postData.put("service_price_type", job_price_type);
            postData.put("service_price", job_price);
            postData.put("service_availability", job_availability);
            System.out.println("postData.put shitit laitettu");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addJobUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                System.out.println("onnistui");
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

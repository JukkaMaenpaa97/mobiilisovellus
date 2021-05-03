package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JobInfo extends AppCompatActivity {

    private TextView mTextViewResult;
    private TextView mTextViewDescription;
    private TextView mTextViewPricing;
    private RequestQueue mQueue;
    private String url;
    Globals g = Globals.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);
        url = getIntent().getStringExtra("keyurl");
        mTextViewResult = findViewById(R.id.header);
        mTextViewDescription = findViewById(R.id.jobInfoTextView);
        mTextViewPricing = findViewById(R.id.pricingTextView);
        mQueue = Volley.newRequestQueue(this);
        jsonParse(url);

    }

    private void jsonParse(String url){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            JSONObject job = jsonArray.getJSONObject(0);   //tähän indeksi, monennenko työn haluaa (0=eka 1=toka jne. tullaan vaihtamaan id:llä haettavaksi)
                            String jobName = job.getString("service_title");
                            Double jobPrice = job.getDouble("service_price");
                            String jobPriceType = job.getString("service_type_string");
                            String jobDescription = job.getString("service_description");
                            String jobAvailability = job.getString("service_availability");
                            mTextViewResult.setText(jobName);
                            String description = jobDescription + "\n\nSaatavuus:\n" + jobAvailability;
                            mTextViewDescription.setText(description);
                            String pricing = jobPriceType + "\n" + jobPrice.toString() + " €";
                            mTextViewPricing.setText(pricing);
                        } catch (JSONException e) {
                            System.out.println("\nnyt ollaan onResponsen catchissä: JSONExceptionissa siis\n");
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

    public void homeButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view)
    {
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

    public void purchaseJobButtonClicked(View view) {
        if (g.getApi_key() == null) {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            Toast.makeText(getApplicationContext(),"Et ole kirjautunut tai kirjautumisesi on vanhentunut",Toast.LENGTH_SHORT).show();
            System.out.println(g.getApi_key());
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), ConfirmReservation.class);
            intent.putExtra("url", url);
            System.out.println(g.getApi_key());
            startActivity(intent);
        }
    }

    public void chatButtonClicked(View view) {
        Toast.makeText(getApplicationContext(), "Tämä 'feature' tulee myöhemmin", Toast.LENGTH_SHORT).show();
    }


}
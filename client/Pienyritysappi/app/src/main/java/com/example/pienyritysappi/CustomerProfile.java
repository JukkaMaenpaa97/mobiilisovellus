package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CustomerProfile extends AppCompatActivity {


    private static final Pattern PUHELIN_PATTERN =
            Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");


    private EditText textInputEmail;
    private EditText textInputPassword;
    private EditText textInputUsername;
    private EditText textInputPassword2;
    private EditText textInputNumber;

    private String url= "http://mobiilisovellus.therozor.com:5000/user/me";

    private RequestQueue mQueue;
    private JSONArray userInfo;
    private JSONObject jsonObject;
    private JSONObject postData;
    private String api_key;

    Globals g = Globals.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        api_key = g.getApi_key();
        if (api_key == "") {
            Toast.makeText(getApplicationContext(), "Kirjautumisesi on vanhentunut", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        mQueue = Volley.newRequestQueue(this);

        textInputEmail = findViewById(R.id.editTextTextEmailAddress2);
        textInputPassword = findViewById(R.id.editTextTextPassword2);
        textInputUsername = findViewById(R.id.editTextTextPersonName3);
        textInputPassword2 = findViewById(R.id.editTextTextPassword3);
        textInputNumber = findViewById(R.id.muokaanumero);

        EditText mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        mEdit.setEnabled(false);

        EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        mEdit2.setEnabled(false);

        EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
        mEdit3.setEnabled(false);

        EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
        mEdit4.setEnabled(false);

        EditText mEdit5 = (EditText) findViewById(R.id.muokaanumero);
        mEdit5.setEnabled(false);

        getUserProfileInfo();
    }

    private boolean tarkastaEmail() {
        String emailInput = textInputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Sähköposti ei voi olla tyhjä");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Sähköposti ei ole pätevä");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean tarkistapuhelin() {

        String phoneInput = textInputNumber.getText().toString().trim();

        if (phoneInput.isEmpty()) {
            textInputNumber.setError("Kenttä ei voi olla tyhjä");
            return false;
        } else if (!PUHELIN_PATTERN.matcher(phoneInput).matches()) {
            textInputNumber.setError("Puhelinnumero ei kelpaa");
            return false;
        } else {
            textInputNumber.setError(null);
            return true;
        }

    }

    private boolean tarkastaNimi() {

        String usernameInput = textInputUsername.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Nimi ei voi olla tyhjä");
            return false;
        } else if (usernameInput.length() > 50) {
            textInputUsername.setError("Nimi liian pitkä");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

   private boolean tarkastaSalasana() {

        String pw = textInputPassword.getText().toString().trim();
        String pw2 = textInputPassword2.getText().toString().trim();

         if (!pw.equals(pw2)) {
            textInputPassword.setError("Salasant eivät täsmää");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }


    public void homeButton2Clicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void tietojenmuokkaus(View view) {
        EditText mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        mEdit.setEnabled(true);

        EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        mEdit2.setEnabled(true);

        EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
        mEdit3.setEnabled(true);

        EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
        mEdit4.setEnabled(true);

        EditText mEdit5 = (EditText) findViewById(R.id.muokaanumero);
        mEdit5.setEnabled(true);
    }

    public void tallennatiedot(View view) {

        String updateUsername;
        String updateUserEmail;
        String updateUserPhone;
        String updateUserPassword;
        String updatePasswordAgain;

            EditText mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
            mEdit.setEnabled(true);

            EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
            mEdit2.setEnabled(true);

            EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
            mEdit3.setEnabled(true);

            EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
            mEdit4.setEnabled(true);

            EditText mEdit5 = (EditText) findViewById(R.id.muokaanumero);
            mEdit5.setEnabled(true);

            if (!tarkastaSalasana() | !tarkastaEmail() | !tarkastaNimi() |  !tarkistapuhelin()) {
                return;
            }

        mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        mEdit.setEnabled(false);

        mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        mEdit2.setEnabled(false);

        mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
        mEdit3.setEnabled(false);

        mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
        mEdit4.setEnabled(false);

        mEdit5 = (EditText) findViewById(R.id.muokaanumero);
        mEdit5.setEnabled(false);



            String input = "Sähköposti: " + textInputEmail.getText().toString();
            input += "\n";
            input += "Nimi: " + textInputUsername.getText().toString();
            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();


            updateUsername = textInputUsername.getText().toString();
            updateUserEmail = textInputEmail.getText().toString();
            updateUserPhone = textInputNumber.getText().toString();
            updateUserPassword = textInputPassword.getText().toString();
            updatePasswordAgain = textInputPassword2.getText().toString();
            String updateAddress = "a";
            String updateCity = "b";
            String updatePostalCode = "c";
            String updateCompanyId = "d";
            String updateCompanyName = "e";


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postData = new JSONObject();
        try
        {
            postData.put("user_name", updateUsername);
            postData.put("user_email",updateUserEmail);
            postData.put("user_phone",updateUserPhone);
            postData.put("user_address", updateAddress);
            postData.put("user_city",updateCity);
            postData.put("user_postalcode",updatePostalCode);
            postData.put("user_company_id",updateCompanyId);
            postData.put("user_company_name",updateCompanyName);
            postData.put("user_password",updateUserPassword);
            postData.put("user_password_again",updatePasswordAgain);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                System.out.println(" tietojen muokkaus onninstui muokkaus onnistui");
                Toast.makeText(getApplicationContext(), "Tallennus onnistui!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                System.out.println("saveInformationButtonClicked Error Response");
                Toast.makeText(getApplicationContext(), "Tallennus epäonnistui!", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", api_key);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);


    }



       private void getUserProfileInfo()
       {
           JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try {
                       userInfo = response.getJSONArray("data");
                       jsonObject = userInfo.getJSONObject(0);

                       String CustomerName = jsonObject.getString("user_name");
                       String CustomerEmail = jsonObject.getString("user_email");
                       //String CustomerPw = jsonObject.getString("user_password");
                       String CustomerPhone = jsonObject.getString("user_phone");



                       textInputEmail.setText(CustomerEmail);
                       textInputNumber.setText(CustomerPhone);
                       textInputUsername.setText(CustomerName);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   error.printStackTrace();
                   System.out.println("getCompanyProfileInfo Error Response");
               }
           })
           {
               @Override
               public Map<String, String> getHeaders() throws AuthFailureError
               {
                   HashMap<String, String> headers = new HashMap<String, String>();
                   headers.put("apikey", api_key);
                   return headers;
               }
           };
           mQueue.add(request);
       }
}






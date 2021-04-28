package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CompanyEditProfile extends AppCompatActivity {

     private static final Pattern PUHELIN_PATTERN =
            Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");

     private EditText textInputCompanyName;
     private EditText textInputEmail;
     private EditText textInputPhone;
     private EditText textInputAddress;
     private EditText textInputPostalCode;
     private EditText textInputCity;
     private EditText textInputPassword;
     private EditText textInputPasswordConfirm;
     private EditText textInputCompanyId;
     private EditText textInputCompanyDescription;
     private EditText textInputCompanySalesRep;

     private String url= "http://mobiilisovellus.therozor.com:5000/user/me";
     private RequestQueue mQueue;
     private JSONArray userInfo;
     private JSONObject getData;
     private JSONObject postData;
     private String api_key;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);
        api_key = "A5NG1QCBjxNwikVq2zocyAOtGXw3oZCm";
        mQueue = Volley.newRequestQueue(this);

        textInputAddress = findViewById(R.id.companyAddressEditText);
        textInputCity = findViewById(R.id.companyCityEditText);
        textInputCompanyDescription = findViewById(R.id.companyDescriptionEditText);
        textInputCompanyId = findViewById(R.id.companyIdEditText);
        textInputCompanyName = findViewById(R.id.companyNameEditText);
        textInputEmail = findViewById(R.id.companyEmailEditText);
        textInputPassword = findViewById(R.id.companyPasswordEditText);
        textInputPasswordConfirm = findViewById(R.id.companyPasswordConfirmEditText);
        textInputPhone = findViewById(R.id.companyPhoneEditText);
        textInputPostalCode = findViewById(R.id.companyPostalCodeEditText);
        textInputCompanySalesRep = findViewById(R.id.companyAdminEditText);

        textInputAddress.setEnabled(false);
        textInputCity.setEnabled(false);
        textInputCompanyDescription.setEnabled(false);
        textInputCompanyId.setEnabled(false);
        textInputCompanyName.setEnabled(false);
        textInputPassword.setEnabled(false);
        textInputPasswordConfirm.setEnabled(false);
        textInputPostalCode.setEnabled(false);
        textInputPhone.setEnabled(false);
        textInputEmail.setEnabled(false);
        textInputCompanySalesRep.setEnabled(false);

        getCompanyProfileInfo();

    }

    public void homeButtonClicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private boolean checkAddress()
    {
        String companyAddress  = textInputAddress.getText().toString().trim();

        if(companyAddress.isEmpty())
        {
            textInputAddress.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else
        {
            textInputAddress.setError(null);
            return true;
        }
    }

    private boolean checkPostalCode()
    {
        String postalCode;
        postalCode = textInputPostalCode.getText().toString().trim();

        if(postalCode.isEmpty())
        {
            textInputPostalCode.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if(postalCode.length() > 5 || postalCode.length() < 5)
        {
            textInputPostalCode.setError("Postinumeron pitää olla viisi numeroa pitkä");
            return false;
        }

        try
        {
            Integer.parseInt(postalCode);
            textInputPostalCode.setError(null);
            return true;
        }
        catch (NumberFormatException e)
        {
            System.out.println("String ei ole numero");
            textInputPostalCode.setError("Postinumerossa ei voi olla kirjaimia");

        }
        return false;
    }

    private boolean checkCity()
    {
        String companyCity = textInputCity.getText().toString().trim();

        if(companyCity.isEmpty())
        {
            textInputCity.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else
        {
            textInputCity.setError(null);
            return true;
        }
    }

    private boolean checkPhoneNumber()
    {
        String companyPhone = textInputPhone.getText().toString().trim();

        if(companyPhone.isEmpty()) {
            textInputPhone.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!PUHELIN_PATTERN.matcher(companyPhone).matches())
        {
            textInputPhone.setError("Puhelinnumero ei kelpaa");
            return false;
        }
        else
        {
            textInputPhone.setError(null);
            return true;
        }
    }

    private boolean checkCompanyName()
    {
        String companyNameInput = textInputCompanyName.getText().toString().trim();

        if(companyNameInput.isEmpty())
        {
            textInputCompanyName.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else
        {
            textInputCompanyName.setError(null);
            return true;
        }
    }

    private boolean checkEmail()
    {
        String emailInput = textInputEmail.getText().toString().trim();

        if(emailInput.isEmpty())
        {
            textInputEmail.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
        {
            textInputEmail.setError("Sähköposti ei ole pätevä");
            return false;
        }
        else
        {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean checkPassword()
    {
        String password = textInputPassword.getText().toString().trim();
        String passwordConfirm = textInputPasswordConfirm.getText().toString().trim();

        if(password.isEmpty())
        {
            textInputPassword.setError("Kenttä ei voi olla tyhjä");
            return false;
        }
        else if (!password.equals(passwordConfirm))
        {
            textInputPassword.setError("Salasanat eivat täsmää");
            return false;
        }
        else
        {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void editInformationButtonClicked(View view)
    {
        textInputAddress.setEnabled(true);
        textInputCity.setEnabled(true);
        textInputCompanyDescription.setEnabled(true);
        textInputCompanyId.setEnabled(true);
        textInputCompanyName.setEnabled(true);
        textInputPassword.setEnabled(true);
        textInputPasswordConfirm.setEnabled(true);
        textInputPostalCode.setEnabled(true);
        textInputPhone.setEnabled(true);
        textInputEmail.setEnabled(true);
        textInputCompanySalesRep.setEnabled(true);
    }

    public void saveInformationButtonClicked(View view)
    {
        String updateCompanyName;
        String updateEmail;
        String updatePhone;
        String updateAddress;
        String updatePostalCode;
        String updateCity;
        String updatePassword;
        String updatePasswordAgain;
        String updateCompanyId;
        String updateCompanyDesc;
        String updateSalesRep;

        if (!checkEmail() | !checkCompanyName() | !checkAddress() | !checkCity() | !checkPostalCode() | !checkPhoneNumber() | !checkPassword())
        {
            return;
        }

        textInputAddress.setEnabled(false);
        textInputCity.setEnabled(false);
        textInputCompanyDescription.setEnabled(false);
        textInputCompanyId.setEnabled(false);
        textInputCompanyName.setEnabled(false);
        textInputPassword.setEnabled(false);
        textInputPasswordConfirm.setEnabled(false);
        textInputPostalCode.setEnabled(false);
        textInputPhone.setEnabled(false);
        textInputEmail.setEnabled(false);
        textInputCompanySalesRep.setEnabled(false);

        updateAddress = textInputAddress.getText().toString();
        updateCity = textInputCity.getText().toString();
        //updateCompanyDesc = textInputCompanyDescription.getText().toString();
        updateCompanyId = textInputCompanyId.getText().toString();
        updateCompanyName = textInputCompanyName.getText().toString();
        updateEmail = textInputEmail.getText().toString();
        updatePassword = textInputPassword.getText().toString();
        updatePasswordAgain = textInputPasswordConfirm.getText().toString();
        updatePhone = textInputPhone.getText().toString();
        updatePostalCode = textInputPostalCode.getText().toString();
        updateSalesRep = textInputCompanySalesRep.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postData = new JSONObject();
        try
        {
            postData.put("user_address", updateAddress);
            postData.put("user_city",updateCity);
            postData.put("user_name",updateSalesRep);
           // postData.put("",updateCompanyDesc);
            postData.put("user_company_id",updateCompanyId);
            postData.put("user_company_name",updateCompanyName);
            postData.put("user_email",updateEmail);
            postData.put("user_phone",updatePhone);
            postData.put("user_postalcode",updatePostalCode);
            postData.put("user_password",updatePassword);
            postData.put("user_password_again",updatePasswordAgain);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1= new JsonObjectRequest(Request.Method.PUT, url, postData, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                System.out.println(response);
                System.out.println(" tietojen muokkaus onninstui");
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError e)
            {
                e.printStackTrace();
                System.out.println("saveInformationButtonClicked Error Response");
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
        requestQueue.add(jsonObjectRequest1);
       // requestQueue.add(jsonObjectRequest2);

        Toast.makeText(this, "Tallennus onnistui!", Toast.LENGTH_SHORT).show();
    }


    private void getCompanyProfileInfo()
    {
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    userInfo = response.getJSONArray("data");
                    getData = userInfo.getJSONObject(0);

                    String address = getData.getString("user_address");
                    String postalCode = getData.getString("user_postalcode");
                    String phone = getData.getString("user_phone");
                    String companyName = getData.getString("user_company_name");
                    String companyId = getData.getString("user_company_id");
                    String salesRep = getData.getString("user_name");
                    // String companyDesc = getData.getString(""); //????????
                    String email = getData.getString("user_email");
                    String city = getData.getString("user_city");

                    textInputAddress.setText(address);
                    textInputCity.setText(city);
                    textInputCompanyId.setText(companyId);
                    textInputCompanyName.setText(companyName);
                    //textInputCompanyDescription.setText(companyDesc);
                    textInputEmail.setText(email);
                    textInputPhone.setText(phone);
                    textInputPostalCode.setText(postalCode);
                    textInputCompanySalesRep.setText(salesRep);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                System.out.println("getCompanyProfileInfo other data Error Response");
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

        mQueue.add(request1);

    }
}
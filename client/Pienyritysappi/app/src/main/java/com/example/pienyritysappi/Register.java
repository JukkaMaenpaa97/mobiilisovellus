package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;

public class Register extends AppCompatActivity {

    private static final Pattern PUHELIN_PATTERN =
            Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");
    private static final String TAG = "Register" ;


    private EditText textInputPassword;
    private EditText textInputPassword2;
    private EditText textInputEmail;
    private EditText textInputUsername;
    private EditText textInputPhone;

    Switch switchA;
    Switch switchB;

    private String registerUrl = "http://mobiilisovellus.therozor.com:5000/register";
    private int registerUserType;
    private String registerName;
    private String registerPassword;
    private String registerEmail;
    private String registerPhone;
    private String registerPassword2;

    private  String rekisterierrortext = "Jotaki meni vikaan";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputPassword = findViewById(R.id.editTextTextPassword);
        textInputPassword2 = findViewById(R.id.editTextTextPassword4);
        textInputEmail = findViewById(R.id.editTextTextEmailAddress);
        textInputUsername = findViewById(R.id.editTextTextPersonName);
        textInputPhone = findViewById(R.id.editTextPhone);
        switchA = (Switch) findViewById(R.id.switchasiakas);
        switchB = (Switch) findViewById(R.id.switchtarjoaja);



        switchA.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.i(TAG, "onCheckedChanged: " + isChecked);

            if(isChecked){
                switchB.setChecked(false);
                registerUserType = 1;
            }else {
                switchB.setChecked(true);
                System.out.println("Palveluntarjoaja");
                registerUserType = 2;
            }
        });


        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchA.setChecked(false);
                    registerUserType = 2;
                } else {
                    switchA.setChecked(true);
                    registerUserType = 1;
                }
            }
        });


    }

    public void button8Clicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    private boolean tarkistaSalasana() {

        String pw = textInputPassword.getText().toString().trim();
        String pw2 = textInputPassword2.getText().toString().trim();

        if (pw.isEmpty()) {
            textInputPassword.setError("Salasana ei voi olla tyhjä");
            return false;
        } else if (!pw.equals(pw2)) {
            textInputPassword.setError("Salasant eivät täsmää");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }


    }

    private boolean tarkistapuhelin() {

        String phoneInput = textInputPhone.getText().toString().trim();

        if (phoneInput.isEmpty()) {
            textInputPhone.setError("Kenttä ei voi olla tyhjä");
            return false;
        } else if (!PUHELIN_PATTERN.matcher(phoneInput).matches()) {
                textInputPhone.setError("Puhelinnumero ei kelpaa");
                return false;
        } else {
            textInputPhone.setError(null);
            return true;
        }

    }


    private boolean tarkistaNimi() {

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

    private boolean tarkistaEmail() {

        String email = textInputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            textInputEmail.setError("Sähköposti ei voi olla tyhjä");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputEmail.setError("Sähköposti ei ole pätevä");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }






    public void buttonregister(View view) {


        if (!tarkistaSalasana() | !tarkistaEmail() | !tarkistaNimi() | !tarkistapuhelin()) {
            return;
        }
        String input = "Sähköposti: " + textInputEmail.getText().toString();
        input += "\n";
        input += "Nimi: " + textInputUsername.getText().toString();
        input += "\n";
        input += "Puhelinnumero: " + textInputPhone.getText().toString();
        input += "\n";
        input += "Salasana: " + textInputPassword.getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();

         registerName = textInputUsername.getText().toString();
         registerPhone = textInputPhone.getText().toString();
         registerEmail = textInputEmail.getText().toString();
         registerPassword = textInputPassword.getText().toString();
         registerPassword2 = textInputPassword2.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject Postdata = new JSONObject();
        try {
            Postdata.put("user_type", registerUserType);
            Postdata.put("user_name", registerName);
            Postdata.put("user_password", registerPassword);
            Postdata.put("user_email", registerEmail);
            Postdata.put("user_phone", registerPhone);
            Postdata.put("user_password_again", registerPassword2 );
        } catch(JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, registerUrl, Postdata, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                intent.putExtra("registered_email", registerEmail);
                intent.putExtra("registered_password", registerPassword);
                startActivity(intent);
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    System.out.println("Rekisteröitymisen error response");
                    Toast.makeText(getApplicationContext(),rekisterierrortext, Toast.LENGTH_SHORT).show();
                    try {
                        System.out.println(new String(error.networkResponse.data, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

        });
        requestQueue.add(jsonObjectRequest);
    }




}
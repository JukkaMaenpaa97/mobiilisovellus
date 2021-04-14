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

import java.util.regex.Pattern;

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
            }else {
                switchB.setChecked(true);
            }
        });


        switchB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchA.setChecked(false);
                } else {
                    switchA.setChecked(true);
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
    }



}
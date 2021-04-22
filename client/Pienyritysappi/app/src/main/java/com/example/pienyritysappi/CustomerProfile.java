package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class CustomerProfile extends AppCompatActivity {

    //private static final Pattern PASSWORD_PATTERN =
    // Pattern.compile("^" +
    //"(?=.*[0-9])" +         //at least 1 digit
    //"(?=.*[a-z])" +         //at least 1 lower case letter
    //"(?=.*[A-Z])" +         //at least 1 upper case letter
    //"(?=.*[a-zA-Z])" +      //any letter
    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
    // "(?=\\S+$)" +           //no white spaces
    // ".{4,}" +               //at least 4 characters
    //  "$");

    private static final Pattern PUHELIN_PATTERN =
            Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");


    private EditText textInputEmail;
    private EditText textInputPassword;
    private EditText textInputUsername;
    private EditText textInputPassword2;
    private EditText textInputNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

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


    public void profileButton2Clicked(View view) {
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
    }

    public void tallennatiedot(View view) {


            EditText mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
            mEdit.setEnabled(true);

            EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
            mEdit2.setEnabled(true);

            EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
            mEdit3.setEnabled(true);

            EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
            mEdit4.setEnabled(true);

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

            String input = "Sähköposti: " + textInputEmail.getText().toString();
            input += "\n";
            input += "Nimi: " + textInputUsername.getText().toString();
            Toast.makeText(this, input, Toast.LENGTH_SHORT).show();


        }


    }


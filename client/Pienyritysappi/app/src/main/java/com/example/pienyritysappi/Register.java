package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private EditText textInputPassword;
    private EditText textInputPassword2;
    private EditText textInputEmail;
    private EditText textInputUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textInputPassword = findViewById(R.id.editTextTextPassword);
        textInputPassword2 = findViewById(R.id.editTextTextPassword4);
        textInputEmail = findViewById(R.id.editTextTextEmailAddress);
        textInputUsername = findViewById(R.id.editTextTextPersonName);


    }

    public void button8Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
    }


    private boolean tarkistaSalasana() {

        String pw = textInputPassword.getText().toString().trim();
        String pw2 = textInputPassword2.getText().toString().trim();

        if (pw.isEmpty()) {
            textInputPassword.setError("Salasana ei voi olla tyhjä");
            return false;
        } else if (!pw.equals(pw2))  {
            textInputPassword.setError("Salasant eivät täsmää");
            return false;
        }
        else {
            textInputPassword.setError(null);
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

        if (!tarkistaSalasana() | !tarkistaEmail() | !tarkistaNimi()) {
            return;
        }
        String input = "Sähköposti: " + textInputEmail.getText().toString();
        input += "\n";
        input += "Nimi: " + textInputUsername.getText().toString();
        input += "\n";
        input += "Salasana: " + textInputPassword.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

}
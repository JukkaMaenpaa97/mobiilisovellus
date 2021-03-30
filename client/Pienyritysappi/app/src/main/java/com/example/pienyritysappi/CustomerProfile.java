package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CustomerProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        EditText mEdit = (EditText) findViewById(R.id.editTextTextPersonName3);
        mEdit.setEnabled(false);

        EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        mEdit2.setEnabled(false);

        EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
        mEdit3.setEnabled(false);

        EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
        mEdit4.setEnabled(false);

    }


    public void profileButton2Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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
        mEdit.setEnabled(false);

        EditText mEdit2 = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        mEdit2.setEnabled(false);

        EditText mEdit3 = (EditText) findViewById(R.id.editTextTextPassword2);
        mEdit3.setEnabled(false);

        EditText mEdit4 = (EditText) findViewById(R.id.editTextTextPassword3);
        mEdit4.setEnabled(false);

    }
}
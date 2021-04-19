package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfirmReservation extends AppCompatActivity {

    private int index = 0;
    private TextView tvHeader;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reservation);
        Intent intent;
        index = getIntent().getIntExtra("indeksi",0);
        System.out.println(index);
        tvHeader = findViewById(R.id.textViewConfirmHeader);
        tvInfo = findViewById(R.id.textViewConfirmInfo);
        String i = Integer.toString(index);
        tvHeader.setText(i);
        tvInfo.setText(i);

    }

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void ConfirmClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
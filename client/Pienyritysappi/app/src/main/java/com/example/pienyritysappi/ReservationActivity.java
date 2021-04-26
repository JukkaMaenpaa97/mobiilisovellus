package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class ReservationActivity extends AppCompatActivity {

    private Button nButton;
    private int eec = 0;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        url = getIntent().getStringExtra("url");
        System.out.println(url);
        for(int i = 0; i < 60; i++){
            addButton(i);
        }
    }

    private void addButton(int i) {
        GridLayout layout = (GridLayout)findViewById(R.id.reservationGridLayout);
        nButton = new Button(this);
        String buttonText = "aika " + Integer.toString(i+1);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(10,10,10,10);
        nButton.setLayoutParams(params);
        nButton.setText(buttonText);
        nButton.setTextSize(20);
        nButton.setBackground(nButton.getContext().getDrawable(R.drawable.rounded_button));
        nButton.setPadding(30,0,30,0);
        nButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmReservation.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
        layout.addView(nButton);
    }

    public void homeButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void profileButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CustomerProfile.class);
        startActivity(intent);
    }

    public void supersecreteastereggasmr(View view) {
        eec++;
        String teksti = "no mitäs vittua nyt taas: " + Integer.toString(eec);
        Toast.makeText(this, teksti, Toast.LENGTH_SHORT).show();
    }
}
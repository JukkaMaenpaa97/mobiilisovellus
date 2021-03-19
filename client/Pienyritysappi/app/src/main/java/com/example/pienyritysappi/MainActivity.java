package com.example.pienyritysappi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button1Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Category.class);
        startActivity(intent);
    }
    public void button2Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Company.class);
        startActivity(intent);
    }
    public void button3Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),JobOrder.class);
        startActivity(intent);
    }
    public void button4Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }
    public void button5Clicked(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Services.class);
        startActivity(intent);
    }
}
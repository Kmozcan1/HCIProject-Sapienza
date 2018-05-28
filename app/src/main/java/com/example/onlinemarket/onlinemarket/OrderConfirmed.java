package com.example.onlinemarket.onlinemarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OrderConfirmed extends AppCompatActivity {

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);


        final Intent productList = new Intent(this, ProductActivity.class);

        Button confirm_button = findViewById(R.id.back_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                startActivity(productList);
            }
        });

    }
}

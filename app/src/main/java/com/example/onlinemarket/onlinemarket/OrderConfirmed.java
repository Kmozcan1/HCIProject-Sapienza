package com.example.onlinemarket.onlinemarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OrderConfirmed extends AppCompatActivity {

    User user;
    @Override
    public void onBackPressed() {
        Intent gotoScreenVar = new Intent(OrderConfirmed.this, MainActivity.class);

        gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        gotoScreenVar.putExtra("User", user);
        startActivity(gotoScreenVar);
    }
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
        user = (User) getIntent().getSerializableExtra("user");


        final Intent productList = new Intent(this, ProductActivity.class);

        Button confirm_button = findViewById(R.id.back_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent gotoScreenVar = new Intent(OrderConfirmed.this, MainActivity.class);
                gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                gotoScreenVar.putExtra("User", user);
                startActivity(gotoScreenVar);
            }
        });

    }
}

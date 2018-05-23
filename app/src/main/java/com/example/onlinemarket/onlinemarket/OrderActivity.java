package com.example.onlinemarket.onlinemarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        User user = (User) getIntent().getSerializableExtra("user");
        Order order = (Order) getIntent().getSerializableExtra("order");

    }
}

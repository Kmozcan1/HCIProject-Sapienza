package com.example.onlinemarket.onlinemarket;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        final User user = (User)getIntent().getSerializableExtra("user");
        //final Order order = (Order)getIntent().getSerializableExtra("order");
        final Order order = Utilities.Companion.getOrder();

        TextView user_name = findViewById(R.id.user_name);
        user_name.setText(R.string.blank);
        user_name.append(user.getFirstName() + " " + user.getLastname());

        TextView user_email = findViewById(R.id.user_email);
        user_email.setText(R.string.blank);
        user_email.append(user.getEmail());

        TextView user_address = findViewById(R.id.user_address);
        user_address.setText(R.string.blank);
        user_address.append(user.getAddress());

        TextView user_city = findViewById(R.id.user_city);
        user_city.setText(R.string.blank);
        user_city.append(user.getCity());

        TextView order_company = findViewById(R.id.company_name_order);
        order_company.setText(R.string.blank);
        order_company.append(order.getCompanyName());


        ListView order_list = findViewById(R.id.order_list);
        int productCount = order.getProducts().size();


        final HashMap<Product, Integer> productList = order.getProducts();
        final HashMap<String, Integer> orderProducts = new HashMap<String, Integer>();
        final String[] products = new String[productCount];
        int count = 0;



        for (Map.Entry<Product, Integer> entry : productList.entrySet()) {
            Product key = entry.getKey();
            Integer value = entry.getValue();

            orderProducts.put(key.getProductKey(), value);

            products[count] = ( value.toString() + "x " + key.productName + "    " + String.format("%.02f",key.price*value)+"€");
            count++;
        }

        ArrayList<String> list = new ArrayList<>();
        list.add("");
        for (int i = 0; i < products.length; i++) {
            list.add(products[i]);
        }
        list.add("");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        order_list.setAdapter(adapter);


        TextView order_total = (TextView)findViewById(R.id.order_total);
        order_total.setText(R.string.blank);
        order_total.append("€" + (Math.round(order.getTotalPrice()*100.0)/100.0));

        final Intent orderConfirm = new Intent(this, OrderConfirmed.class);

        Button confirm_button = (Button) findViewById(R.id.confirm_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
                String orderID = ref.push().getKey();

                order.setUserEmail(user.getEmail());
                order.setZone(user.getZone());
                order.setAddress((user.getAddress()));
                order.setDone(true);


                Map<String, Object> orderData = new HashMap<String, Object>();
                orderData.put("email",  order.getUserEmail());
                orderData.put("price", order.getTotalPrice());
                orderData.put("zone", order.getZone());
                orderData.put("time", order.getTime());
                orderData.put("address", order.getAddress());
                orderData.put("isDone", false);
                orderData.put("companyName", order.getCompanyName());

                ref.child(orderID).setValue(orderData);
                for (Map.Entry<Product, Integer> entry : productList.entrySet()) {
                    Product key = entry.getKey();
                    Integer value = entry.getValue();

                    ref.child(orderID).child("orderedproducts").child(key.getProductKey()).setValue(key);
                    ref.child(orderID).child("orderedproducts").child(key.getProductKey()).child("quantity").setValue(value);
                    ref.child(orderID).child("orderedproducts").child(key.getProductKey()).child("productKey").removeValue();
                }
                orderConfirm.putExtra("user", user);
                startActivity(orderConfirm);
            }
        });

    }
}

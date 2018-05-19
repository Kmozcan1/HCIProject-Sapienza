package com.example.onlinemarket.onlinemarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final ArrayList<Product> products;
    private final TextView totalPriceText;
    public GridAdapter(Context context, ArrayList<Product> products, TextView totalPriceText) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
        this.totalPriceText= totalPriceText;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.product_list_item, parent , false);

        final Product singleProductItem = getItem(position);
        final TextView nameText = customView.findViewById(R.id.productNameLabel);
        ImageView productImage = customView.findViewById(R.id.productImage);
        TextView productPrice = customView.findViewById(R.id.productPriceLabel);
        Button increaseButton= customView.findViewById(R.id.increaseButton);
        Button decreaseButton =customView.findViewById(R.id.decreaseButton);
        final TextView numberText= customView.findViewById(R.id.numberText);

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number =Integer.parseInt((String) numberText.getText());
                number++;
                numberText.setText(String.valueOf(number));
                String lastPrice = calculatePrice(totalPriceText.getText().toString(), singleProductItem.price, true).toString() + " €";
                totalPriceText.setText(lastPrice);
                if(number==1)
                    numberText.setBackgroundColor(Color.rgb(0,255,0));
            }
        });
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number =Integer.parseInt((String) numberText.getText());
                if(number!=0) {
                    number--;
                    String lastPrice = calculatePrice(totalPriceText.getText().toString(), singleProductItem.price, false).toString() + " €";
                    totalPriceText.setText(lastPrice);
                    numberText.setText(String.valueOf(number));
                    if(number==0)
                        numberText.setBackgroundColor(Color.parseColor("#F0F0F0"));
                }
            }
        });

        nameText.setText(singleProductItem.getProductName());

        productImage.setImageBitmap(imageTransform.StringToBitmap(singleProductItem.getProductImage()));
        productPrice.setText("Price: " + singleProductItem.getPrice().toString() + "€");

        return customView;

    }
    public Double calculatePrice(String totalPrice, Double productPrice, Boolean Increment){
        Double totalPriceDouble = Double.parseDouble((totalPrice.replace("€", "")).replace(" ",""));
        if (Increment)
            totalPriceDouble= totalPriceDouble+ productPrice;
        else
            totalPriceDouble= totalPriceDouble- productPrice;
        return Math.round(totalPriceDouble*100.0)/100.0;
    }


}

package com.example.onlinemarket.onlinemarket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class GridAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final ArrayList<Product> products;
    private final TextView totalPriceText;
    private final TextView countText;
    public Order order;
    public ArrayList<HashMap<Integer,Integer>> hashMaps= new ArrayList<>();
    public int tabID;

    public GridAdapter(Context context, ArrayList<Product> products, TextView totalPriceText, TextView countText ,int tabID, int tabCount, Order order) {
        super(context, 0, products);
        this.order=order;
        this.context = context;
        this.products = products;
        this.totalPriceText= totalPriceText;
        this.countText = countText;
        this.tabID = tabID;
        for(int i=0; i<tabCount;i++)
            hashMaps.add(new HashMap<Integer,Integer>() );
        for(int i=0;i<products.size();i++)
        {
            hashMaps.get(tabID).put(i,0);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
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
        numberText.setText(hashMaps.get(tabID).get(position).toString());
        if(hashMaps.get(tabID).get(position)>0) {
            numberText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            numberText.setTextColor(Color.WHITE);
        }


        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number =hashMaps.get(tabID).get(position);
                number++;
                order.InsertProduct(singleProductItem);
                hashMaps.get(tabID).put(position,number);
                numberText.setText(hashMaps.get(tabID).get(position).toString());
                String lastPrice = calculatePrice(totalPriceText.getText().toString(), singleProductItem.price, true).toString() + " €";
                totalPriceText.setText(lastPrice);
                int lastCount = Integer.parseInt(countText.getText().toString());
                lastCount++;
                countText.setText(String.valueOf(lastCount));

                if(hashMaps.get(tabID).get(position)==1) {
                    numberText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    numberText.setTextColor(Color.WHITE);
                }


            }
        });
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number =hashMaps.get(tabID).get(position);
                if(number!=0) {
                    order.DeleteProduct(singleProductItem);
                    number--;
                    hashMaps.get(tabID).put(position,number);
                    String lastPrice = calculatePrice(totalPriceText.getText().toString(), singleProductItem.price, false).toString() + " €";
                    totalPriceText.setText(lastPrice);
                    int lastCount = Integer.parseInt(countText.getText().toString());
                    if(lastCount!=0) {
                        lastCount--;
                        countText.setText(String.valueOf(lastCount));
                    }
                    numberText.setText(hashMaps.get(tabID).get(position).toString());
                    if(hashMaps.get(tabID).get(position)==0) {
                        numberText.setBackgroundColor(Color.parseColor("#F0F0F0"));
                        numberText.setTextColor(Color.BLACK);
                    }
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

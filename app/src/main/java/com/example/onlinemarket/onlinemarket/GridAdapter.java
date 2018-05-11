package com.example.onlinemarket.onlinemarket;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final ArrayList<Product> products;
    public GridAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.product_list_item, parent , false);

        Product singleProductItem = getItem(position);
        TextView nameText = customView.findViewById(R.id.productNameLabel);
        ImageView productImage = customView.findViewById(R.id.productImage);
        TextView productPrice = customView.findViewById(R.id.productPriceLabel);


        nameText.setText(singleProductItem.getProductName());
        productImage.setImageBitmap(imageTransform.StringToBitmap(singleProductItem.getProductImage()));
        productPrice.setText(singleProductItem.getPrice().toString());

        return customView;

    }


}

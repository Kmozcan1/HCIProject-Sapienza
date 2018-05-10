package com.example.onlinemarket.onlinemarket;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;

class CompanyListViewAdapter extends ArrayAdapter<Company> {
    private final Context context;
    private final ArrayList<Company> companies;

    CompanyListViewAdapter(Context context, ArrayList<Company> companies) {
        super(context, 0, companies);
        this.context = context;
        this.companies = companies;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.company_list_item, parent , false);

        Company singleCompanyItem = getItem(position);
        TextView companyText = customView.findViewById(R.id.company_name_label);
        ImageView companyImage = customView.findViewById(R.id.company_image);

        companyText.setText(singleCompanyItem.getCompanyName());
        companyImage.setImageBitmap(imageTransform.StringToBitmap(singleCompanyItem.getImage()));

        return customView;

    }
}

package com.example.onlinemarket.onlinemarket;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
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
        TextView openText = customView.findViewById(R.id.open_time_label);
        TextView closeText= customView.findViewById(R.id.close_time_label);
        TextView openorcloseText = customView.findViewById(R.id.openorclosed_label);


        companyText.setText(singleCompanyItem.getCompanyName());
        companyImage.setImageBitmap(imageTransform.StringToBitmap(singleCompanyItem.getImage()));
        openText.setText(singleCompanyItem.getOpenTime());
        closeText.setText(singleCompanyItem.getCloseTime());

        if(singleCompanyItem.calculateOpenOrClosed()=="Open")
        {
            openorcloseText.setText("Open");
            openorcloseText.setTextColor(Color.rgb(0,200,0));
        }
        else{
            openorcloseText.setText("Closed");
            openorcloseText.setTextColor(Color.rgb(200,0,0));
        }

        return customView;

    }
}

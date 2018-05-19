package com.example.onlinemarket.onlinemarket

import android.app.ListActivity
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


@RequiresApi(api = Build.VERSION_CODES.O)
class ProductSearchListViewAdapter(context: Context, resource: Int, list: MutableList<ProductListItemData>) :
        ArrayAdapter<ProductListItemData>(context, resource, list), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        //Get the view
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.productsearch_list_item, parent, false)

        //Get the item
        val productListItem = getItem(position)

        //Find layout elements
        val productImageView = view.findViewById<ImageView>(R.id.productImage_imageView)
        val productNameTextView = view.findViewById<TextView>(R.id.productName_textView)
        val companyImageView = view.findViewById<ImageView>(R.id.companyImage_imageView)

        //Fill layout elements
        productImageView.setImageBitmap(imageTransform.StringToBitmap(productListItem.productImage))
        productNameTextView.text = productListItem.productName
        companyImageView.setImageBitmap(imageTransform.StringToBitmap(productListItem.companyImage))

        return view
    }
}


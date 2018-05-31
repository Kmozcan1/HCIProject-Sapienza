package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView





@RequiresApi(api = Build.VERSION_CODES.O)
class OrderedProductListViewAdapter(context: Context, resource: Int, list: MutableList<OrderedProductData>) :
        ArrayAdapter<OrderedProductData>(context, resource, list), Filterable {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.orderedproduct_list_item, parent, false)

        //Find layout elements
        val orderedProductNameTextView = view.findViewById<TextView>(R.id.orderedProductName_textView)
        val orderedProductPriceTextView = view.findViewById<TextView>(R.id.orderedProductPrice_textView)

        //Fill layout elements
        val product = getItem(position)
        orderedProductNameTextView.text = "x" + product!!.quantity + " " + product!!.productName
        orderedProductPriceTextView.text = (product.price!!.times(product!!.quantity).toString()) + " €"
        return view
    }
}
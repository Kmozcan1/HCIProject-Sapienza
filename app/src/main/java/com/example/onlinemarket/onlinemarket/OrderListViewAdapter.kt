package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView

@RequiresApi(api = Build.VERSION_CODES.O)
class OrderListViewAdapter(context: Context, resource: Int, list: MutableList<Order>) :
        ArrayAdapter<Order>(context, resource, list), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        //Get the view
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.orderhistory_list_item, parent, false)

        //Get the item
        val order = getItem(position)

        //Find layout elements
        val orderAddressTextView = view.findViewById<TextView>(R.id.orderAddress_textView)
        val orderPriceTextView = view.findViewById<TextView>(R.id.orderPrice_textView)
        val orderStatusTextView = view.findViewById<TextView>(R.id.orderStatus_textView)
        val companyImageView = view.findViewById<ImageView>(R.id.OrderListCompanyImage_imageView)
        val orderNoTextView = view.findViewById<TextView>(R.id.orderNo_textView)

        //Fill layout elements
        orderAddressTextView.text = order.address
        orderPriceTextView.text = order.totalPrice.toString() + " €"
        orderNoTextView.text = order.orderNo
        companyImageView.setImageBitmap(imageTransform.StringToBitmap(order.companyImage))

        if (order.done) {
            orderStatusTextView.setTextColor(Color.GREEN)
            orderStatusTextView.text = "Order Delivered"
        }
        else {
            orderStatusTextView.setTextColor(Color.RED)
            orderStatusTextView.text = "Order En Route"
        }

        return view
    }
}

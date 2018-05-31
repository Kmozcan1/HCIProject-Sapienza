package com.example.onlinemarket.onlinemarket

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_order_list.*
import kotlinx.android.synthetic.main.dialog_orderdetails.*

class OrderDetailsDialogFragment: DialogFragment(), DialogFragmentListener {

    private var order: Order? = null

    override fun handleDialogClose(dialog: DialogInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun newInstance(order: Order): OrderDetailsDialogFragment {
        val frag = OrderDetailsDialogFragment()
        this.order = order
        return frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_orderdetails, null)

        if (Utilities.activeUser != null) {
            if (Utilities.activeUser!!.email == "admin@admin") {
                builder.setView(view)
                        .setPositiveButton(R.string.setStatusToDeliveredButton, object: DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                Utilities.updateOrder(order!!.orderKey)
                                getDialog().dismiss()
                                Toast.makeText(context,
                                        "Order Status Updated", Toast.LENGTH_LONG).show()
                            }
                        })
            }
        }

        //Fill out the TextViews
        val orderDateValueTextView = view.findViewById<TextView>(R.id.orderDateValue_textView)
        val orderCompanyValueTextView = view.findViewById<TextView>(R.id.orderCompanyValue_textView)
        val orderTotalPriceValueTextView = view.findViewById<TextView>(R.id.orderTotalPriceValue_textView)
        val orderAddressValueTextView = view.findViewById<TextView>(R.id.orderAddressValue_textView)
        orderDateValueTextView.text = order!!.time
        orderCompanyValueTextView.text = order!!.companyName
        orderTotalPriceValueTextView.text = order!!.totalPrice.toString() + " €"
        orderAddressValueTextView.text = order!!.address

        val orderedProductListViewAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OrderedProductListViewAdapter(context,
                    R.layout.orderedproduct_list_item, order!!.orderedProducts)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        view.findViewById<ListView>(R.id.orderedproducts_listView).adapter = orderedProductListViewAdapter
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity = activity
        if (activity is OrderListActivity)
            (activity as DialogFragmentListener).handleDialogClose(dialog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            this.dismiss()
        }
    }


}
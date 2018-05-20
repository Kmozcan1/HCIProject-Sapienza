package com.example.onlinemarket.onlinemarket

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView


class ProductDetailsDialogFragment: DialogFragment(), DialogFragmentListener {

    private var productListItemData: ProductListItemData? = null

    override fun handleDialogClose(dialog: DialogInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun newInstance(productListItemData: ProductListItemData): ProductDetailsDialogFragment {
        val frag = ProductDetailsDialogFragment()
        this.productListItemData = productListItemData
        return frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_productdetails, null)

        builder.setView(view)
                .setPositiveButton(R.string.button_edit, object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(activity, InsertProductActivity::class.java)
                        intent.putExtra("from", "productList")
                        intent.putExtra("productKey", productListItemData!!.productKey)
                        startActivityForResult(intent, 0)
                    }
                })

        //Fill out the TextViews
        val productNameTextView = view.findViewById<TextView>(R.id.productNameValue_textView)
        val productPriceTextView = view.findViewById<TextView>(R.id.productPriceValue_textView)
        val productCompanyTextView = view.findViewById<TextView>(R.id.productCompanyValue_textView)
        productNameTextView.text = productListItemData!!.productName
        productPriceTextView.text = productListItemData!!.productPrice.toString() + " €"
        productCompanyTextView.text = productListItemData!!.companyName


        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity = activity
        if (activity is ProductListActivity)
            (activity as DialogFragmentListener).handleDialogClose(dialog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            this.dismiss()
        }
    }
}
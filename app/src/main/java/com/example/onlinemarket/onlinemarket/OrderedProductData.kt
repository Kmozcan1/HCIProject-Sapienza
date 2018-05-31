package com.example.onlinemarket.onlinemarket

import android.os.Parcel
import android.os.Parcelable
import com.example.onlinemarket.onlinemarket.R.id.productImage
import com.example.onlinemarket.onlinemarket.R.string.productName

/**
 * Created by:  Kadir Mert Ozcan
 * Used in:     ProductSearchListViewAdapter
 * */

data class OrderedProductData(val productImage: String, var quantity: Int,
                              val price: Double, val company: String,
                              var category: String, var productName: String)
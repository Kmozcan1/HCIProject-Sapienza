package com.example.onlinemarket.onlinemarket

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by:  Kadir Mert Ozcan
 * Used in:     ProductSearchListViewAdapter
 * */
data class ProductListItemData(val productKey: String, val productName: String,
                               val productImage: String, val productPrice: Double,
                               var companyName: String, var companyImage: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productKey)
        parcel.writeString(productName)
        parcel.writeString(productImage)
        parcel.writeDouble(productPrice)
        parcel.writeString(companyName)
        parcel.writeString(companyImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductListItemData> {
        override fun createFromParcel(parcel: Parcel): ProductListItemData {
            return ProductListItemData(parcel)
        }

        override fun newArray(size: Int): Array<ProductListItemData?> {
            return arrayOfNulls(size)
        }
    }
}
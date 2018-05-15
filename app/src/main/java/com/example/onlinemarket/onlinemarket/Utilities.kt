package com.example.onlinemarket.onlinemarket

import android.content.Context
import com.example.onlinemarket.onlinemarket.R.string.productName
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase


object Utilities {
    fun getCompanyByProduct(context: Context, companyName: String): String {

        val reference = FirebaseDatabase.getInstance().reference

        val query = reference.child("companies").orderByChild("companyName").equalTo(companyName)
        var imageString = ""
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageString = dataSnapshot.child("image").value.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return imageString

    }

    fun getCompanies(listener: FireBaseListener) {
        val companyList = ArrayList<Company?>()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("companies")
        query!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    for (companyObj in p0.children) {
                        val name = companyObj.child("companyName").getValue(String::class.java)
                        val image = companyObj.child("image").getValue(String::class.java)
                        val openTime = companyObj.child("openTime").getValue(String::class.java)
                        val closeTime = companyObj.child("closeTime").getValue(String::class.java)
                        val company = Company(name, image, openTime, closeTime)
                        companyList.add(company)
                        listener.onCallBack(companyList)
                    }
                }
            }
        })
    }

    fun getProducts(listener: FireBaseListener){
        val productList = ArrayList<Product?>()
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("products")
        query!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    for (productObj in p0.children) {
                        val category = productObj.child("category").getValue(String::class.java)
                        val company = productObj.child("company").getValue(String::class.java)
                        val price = productObj.child("price").getValue(Double::class.java)
                        val productImage = productObj.child("productImage").getValue(String::class.java)
                        val productName = productObj.child("productName").getValue(String::class.java)
                        val product = Product(productName, price, company, productImage, category)
                        productList.add(product)
                        listener.onCallBack(productList)
                    }
                }
            }
        })
    }
}
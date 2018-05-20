package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.util.Log
import com.example.onlinemarket.onlinemarket.R.id.snap
import com.example.onlinemarket.onlinemarket.R.string.productName
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase


class Utilities {
    companion object {
        private val fireBaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        init {
            if (fireBaseDatabase == null) {
                fireBaseDatabase.setPersistenceEnabled(true)
            }
        }

        fun getDatabase() : FirebaseDatabase? {
            return fireBaseDatabase
        }

        fun getProducts(listener: FireBaseListener){
            val productList = ArrayList<Product?>()
            val reference = getDatabase()!!.reference
            val query = reference.child("products")
            query!!.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.exists()) {
                        productList.clear()
                        for (productObj in p0.children) {
                            val productKey = productObj.key
                            val category = productObj.child("category").getValue(String::class.java)
                            val company = productObj.child("company").getValue(String::class.java)
                            val price = productObj.child("price").getValue(Double::class.java)
                            val productImage = productObj.child("productImage").getValue(String::class.java)
                            val productName = productObj.child("productName").getValue(String::class.java)
                            val product = Product(productKey, productName, price, company, productImage, category)
                            productList.add(product)
                        }
                        listener.onCallBack(productList)
                    }
                }
            })
        }

        fun getSingleProduct(key: String, listener: FireBaseListener) {
            val reference = getDatabase()!!.reference
            var query = reference.child("products").child(key)
            query!!.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(product: DataSnapshot?) {
                    if(product!!.exists()) {
                        val productName = product.child("productName").getValue(String::class.java)
                        val price = product.child("price").getValue(Double::class.java)
                        val company = product.child("company").getValue(String::class.java)
                        val productImage = product.child("productImage").getValue(String::class.java)
                        val category = product.child("category").getValue(String::class.java)
                        val product = Product(key, productName, price, company, productImage, category)
                        listener.onCallBack(product)
                    }
                }
            })
        }

        fun updateProduct(product: Product) {
            val reference = getDatabase()!!.reference
            var query = reference.child("products").child(product.productKey)
            query.child("productName").setValue(product.productName)
            query.child("price").setValue(product.price)
            query.child("company").setValue(product.company)
            query.child("productImage").setValue(product.productImage)
            query.child("category").setValue(product.category)
        }

        fun getCompanies(listener: FireBaseListener) {
            val companyList = ArrayList<Company?>()
            val reference = getDatabase()!!.reference

            val query = reference.child("companies")
            query!!.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.exists()) {
                        companyList.clear()
                        for (companyObj in p0.children) {
                            val name = companyObj.child("companyName").getValue(String::class.java)
                            val image = companyObj.child("image").getValue(String::class.java)
                            val openTime = companyObj.child("openTime").getValue(String::class.java)
                            val closeTime = companyObj.child("closeTime").getValue(String::class.java)
                            val company = Company(name, image, openTime, closeTime)
                            companyList.add(company)
                        }
                        listener.onCallBack(companyList)
                    }
                }
            })
        }
    }
}
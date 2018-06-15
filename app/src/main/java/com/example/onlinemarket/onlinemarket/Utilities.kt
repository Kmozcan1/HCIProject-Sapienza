package com.example.onlinemarket.onlinemarket

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import com.example.onlinemarket.onlinemarket.R.id.order
import com.example.onlinemarket.onlinemarket.R.string.productName
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import java.util.HashMap


class Utilities {
    companion object {
        private val fireBaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        private var staticOrder: Order? = null
        var activeUser: User? = null

        init {
            if (fireBaseDatabase == null) {
                fireBaseDatabase.setPersistenceEnabled(true)
            }
        }

        private fun getDatabase() : FirebaseDatabase? {
            return fireBaseDatabase
        }

        fun getOrder(): Order? {
            return staticOrder
        }

        fun setOrder(orderObject: Order) {
            staticOrder = orderObject
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
                        listener.onCallBack(productList, this, query)
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
                        listener.onCallBack(product, this, query)
                    }
                }
            })
        }

        fun getSingleCompany(key: String, listener: FireBaseListener) {
            val reference = getDatabase()!!.reference
            var query = reference.child("companies").child(key)
            query!!.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(company: DataSnapshot?) {
                    if(company!!.exists()) {
                        val key = company.key
                        val name = company.child("companyName").getValue(String::class.java)
                        val image = company.child("image").getValue(String::class.java)
                        val openTime = company.child("openTime").getValue(String::class.java)
                        val closeTime = company.child("closeTime").getValue(String::class.java)
                        val company = Company(key, name, image, openTime, closeTime)
                        listener.onCallBack(company, this, query)
                    }
                }
            })
        }

        fun removeSingleProduct(key: String) {
            val reference = getDatabase()!!.reference
            var query = reference.child("products").child(key)
            query.removeValue()
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

        fun updateCompany(company: Company) {
            val reference = getDatabase()!!.reference
            var query = reference.child("companies").child(company.companyKey)
            query.child("companyName").setValue(company.companyName)
            query.child("image").setValue(company.image)
            query.child("openTime").setValue(company.openTime)
            query.child("closeTime").setValue(company.closeTime)
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
                            val key = companyObj.key
                            val name = companyObj.child("companyName").getValue(String::class.java)
                            val image = companyObj.child("image").getValue(String::class.java)
                            val openTime = companyObj.child("openTime").getValue(String::class.java)
                            val closeTime = companyObj.child("closeTime").getValue(String::class.java)
                            val company = Company(key, name, image, openTime, closeTime)
                            companyList.add(company)
                        }
                        listener.onCallBack(companyList, this, query)
                    }
                }
            })
        }

        fun getOrders(email: String, listener: FireBaseListener) {
            val orderList = ArrayList<Order?>()
            val reference = getDatabase()!!.reference
            var query = reference.child("orders").orderByChild("email").equalTo(email)
            if (email == "admin@admin") {
                query = reference.child("orders").orderByChild("email")
            }
            query!!.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot?) {
                    if(p0!!.exists()) {
                        orderList.clear()
                        for (orderObj in p0.children) {
                            val key = orderObj.key
                            val address = orderObj.child("address").getValue(String::class.java)
                            val companyName = orderObj.child("companyName").getValue(String::class.java)
                            val email = orderObj.child("email").getValue(String::class.java)
                            val isDone = orderObj.child("isDone").getValue(Boolean::class.java)
                            val price = orderObj.child("price").getValue(Double::class.java)
                            val time = orderObj.child("time").getValue(String::class.java)
                            val zone = orderObj.child("zone").getValue(String::class.java)
                            val orderedProducts = orderObj.child("orderedproducts")
                            var orderedProductData: OrderedProductData? = null
                            val orderedProductList = ArrayList<OrderedProductData?>()
                            if (orderedProducts!!.exists())
                                for (obj in orderedProducts.children) {
                                    val productImage = obj.child("productImage").getValue(String::class.java)
                                    val quantity = obj.child("quantity").getValue(Int::class.java)
                                    val price = obj.child("price").getValue(Double::class.java)
                                    val company = obj.child("company").getValue(String::class.java)
                                    val category = obj.child("category").getValue(String::class.java)
                                    val productName = obj.child("productName").getValue(String::class.java)
                                    orderedProductData = OrderedProductData(productImage!!, quantity!!, price!!, company!!, category!!, productName!!)
                                    orderedProductList.add(orderedProductData)
                                }

                            /*for (product in products!!) {
                                val category = product.category
                                val company = product.company
                                val price = product.price
                                public Order(String orderKey, Boolean isDone, String userEmail, String address, String zone,
                                    String companyName, Double totalPrice, List<Product> productList, String time) {
                            }*/

                            val order = Order(key, isDone, email, address, zone, companyName, price, orderedProductList, time)
                            orderList.add(order)
                        }
                        listener.onCallBack(orderList, this, query.ref)
                    }
                }
            })
        }

        fun updateOrder(orderKey: String) {
            val reference = getDatabase()!!.reference
            var query = reference.child("orders").child(orderKey)
            query.child("isDone").setValue(true)
        }

        fun insertCompany(company: Company) {
            val reference = getDatabase()!!.reference
            val companyID = reference.push().key
            reference.child("companies").child(companyID).setValue(company)
        }

        fun handleProgressBarAction(progressBar: ProgressBar, window: Window, visible: Boolean) {
            when {
                visible -> {
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }

        fun hasPermission(context: Activity, permission: String, requestCode: Int): Boolean {
            return if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
                false
            } else {
                true
            }
        }

        fun openGallery(activity: Activity, packageManager: PackageManager,
                        requestCode: Int, bundleOptions: Bundle?) {
            val intent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(activity, intent, requestCode, bundleOptions)
            }
        }
    }
}
package com.example.onlinemarket.onlinemarket

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_insert_product.*


class InsertProductActivity : AppCompatActivity() {
    var mCompaniesReference : DatabaseReference?= null
    private var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_product)
        fillCompanies()



    }

    private fun fillCompanies() {
        //Fetch the list of items from DB
        val companyList = ArrayList<Company>()
        mDatabase = FirebaseDatabase.getInstance().reference
        mCompaniesReference = FirebaseDatabase.getInstance().getReference("companies")
        mDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    for (cmpObject in p0.children) {
                        val name= cmpObject.child("companyName").getValue(String::class.java)
                        val image= cmpObject.child("image").getValue(String::class.java)
                        val openTime= cmpObject.child("openTime").getValue(String::class.java)
                        val closeTime=cmpObject.child("closeTime").getValue(String::class.java)
                        val cmp = Company(name ,image ,openTime ,closeTime)
                        companyList.add(cmp)
                    }
                    // Create an ArrayAdapter
                    val companyListAdapter = ArrayAdapter(this@InsertProductActivity,
                            android.R.layout.simple_list_item_1, companyList)
                    company_spinner!!.adapter = companyListAdapter
                }
            }


        })

    }
}
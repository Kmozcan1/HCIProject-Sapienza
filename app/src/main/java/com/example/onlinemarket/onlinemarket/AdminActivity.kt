package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        insert_product_button.setOnClickListener {
            intent = Intent(this,InsertProductActivity::class.java)
            startActivity(intent)
        }
        edit_product_button.setOnClickListener {
            intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }
    }
}
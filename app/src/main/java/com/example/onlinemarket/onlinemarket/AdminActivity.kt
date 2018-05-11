package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        button_insert_company.setOnClickListener {
            intent = Intent(this,MainActivity::class.java)
        }

    }
}
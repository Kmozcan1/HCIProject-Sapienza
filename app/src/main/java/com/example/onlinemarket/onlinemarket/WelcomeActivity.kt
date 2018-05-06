package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class WelcomeActivity : AppCompatActivity() {

    var waitingTime : Long ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        waitingTime = 3000
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Handler().postDelayed(object :Runnable{
            public override fun run() {
                val loginActivity = Intent(this@WelcomeActivity,LoginActivity::class.java)
                startActivity(loginActivity)
                finish()
            }


        },waitingTime!!.toLong())

    }
}


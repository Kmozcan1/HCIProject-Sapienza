package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.SharedPreferences



class WelcomeActivity : AppCompatActivity() {

    var waitingTime : Long ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        waitingTime = 3000
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val loginActivity = Intent(this@WelcomeActivity,LoginActivity::class.java)
        Handler().postDelayed(object :Runnable{
            public override fun run() {
                    startActivity(loginActivity)
                    finish()

            }


        },waitingTime!!.toLong())

    }
}


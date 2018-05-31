package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.SharedPreferences
import android.preference.PreferenceManager


class WelcomeActivity : AppCompatActivity() {

    var waitingTime : Long ?= null
    var preferences : SharedPreferences?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        waitingTime = 3000

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val isLogged:Int  = preferences!!.getInt("isLogged",0);
        print("NUM :")
        print(isLogged)
        if(isLogged==1){
            val mainActivity = Intent(this@WelcomeActivity,MainActivity::class.java)
            Handler().postDelayed(object :Runnable{
                public override fun run() {
                    startActivity(mainActivity)
                    finish()

                }


            },waitingTime!!.toLong())

        }
        else{
            val loginActivity = Intent(this@WelcomeActivity,LoginActivity::class.java)
            Handler().postDelayed(object :Runnable{
                public override fun run() {
                    startActivity(loginActivity)
                    finish()

                }


            },waitingTime!!.toLong())

        }


    }
}


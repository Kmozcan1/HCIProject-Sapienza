package com.example.onlinemarket.onlinemarket

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class WelcomeActivity : AppCompatActivity() {

    var waitingTime : Long ?= null
    var preferences : SharedPreferences?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        waitingTime = 3000

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val isLogged:Int  = preferences!!.getInt("isLogged",0);
        val email = preferences!!.getString("email","")
        print("NUM :")
        print(isLogged)
        if(isLogged==1){

            val mainActivity = Intent(this@WelcomeActivity,MainActivity::class.java)
            val FBuserDatabase= FirebaseDatabase.getInstance().getReference("users")
            FBuserDatabase!!.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(p0: DataSnapshot?) {

                    for (usr in p0!!.children) {
                        if(usr.child("email").value == email) {
                            val user:User= User()
                            user.firstName= usr.child("firstName").value.toString()
                            user.lastname= usr.child("lastname").value.toString()
                            user.address= usr.child("address").value.toString()
                            user.city= usr.child("city").value.toString()
                            user.country= usr.child("country").value.toString()
                            user.email= usr.child("email").value.toString()
                            user.userPassword= usr.child("userPassword").value.toString()
                            user.zone= usr.child("zone").value.toString()
                            user.userId = usr.child("userId").value.toString()
                            user.userMobilePhone= usr.child("userMobilePhone").value.toString()

                            Handler().postDelayed(object :Runnable{
                                public override fun run() {
                                    mainActivity.putExtra("User", user)
                                    startActivity(mainActivity)
                                    finish()
                                }
                            },waitingTime!!.toLong())
                        }
                    }
                }

            })
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


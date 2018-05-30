package com.example.onlinemarket.onlinemarket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    var user:User ?= null
    var firstName : String ?= null
    var lastName : String ?= null
    var phone : String ?= null
    var email : String ?= null
    var password : String ?= null
    var country : String ?= null
    var city : String ?= null
    var zone : String ?= null
    var address : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        user= intent.getSerializableExtra("user") as? User

        firstName = user!!.firstName
        lastName = user!!.lastname
        phone = user!!.userMobilePhone
        email = user!!.email
        password = user!!.userPassword
        country = user!!.country
        city = user!!.city
        zone = user!!.zone
        address = user!!.address

        val fnameViev :TextView = findViewById(R.id.firstName);
        fnameViev.setText(firstName)

        val lnameViev :TextView = findViewById(R.id.lastName);
        lnameViev.setText(lastName)

        val phoneViev :TextView = findViewById(R.id.phonenumber);
        phoneViev.setText(phone)

        val mailViev :TextView = findViewById(R.id.email);
        mailViev.setText(email)

        val countryViev :TextView = findViewById(R.id.country);
        countryViev.setText(country)

        val cityViev :TextView = findViewById(R.id.city);
        cityViev.setText(city)

        val zoneViev :TextView = findViewById(R.id.zone);
        zoneViev.setText(zone)

        val addressViev :TextView = findViewById(R.id.address);
        addressViev.setText(address)

    }
}

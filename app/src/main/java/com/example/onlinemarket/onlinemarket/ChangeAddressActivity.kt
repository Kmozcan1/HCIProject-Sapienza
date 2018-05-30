package com.example.onlinemarket.onlinemarket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_change_address.*

class ChangeAddressActivity : AppCompatActivity() {

    var address : String ?= null
    var user:User ?= null
    var newAddress : String ?= null

    var isAdressValid : Boolean ?= null

    var cancel = false
    var focusView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_address)

        addressArea.error=null

        reference= FirebaseDatabase.getInstance().getReference("users")

        user= intent.getSerializableExtra("User") as? User
        address = user!!.address

        val adressView : TextView = findViewById(R.id.addressText);
        adressView.setText(address)

        submitAddress.setOnClickListener(){

            newAddress = addressArea.text.toString()

            if(checkAddress(newAddress!!)==true){
                reference = FirebaseDatabase.getInstance().getReference("users")
                reference!!.child(user!!.userId).child("address").setValue(newAddress)

                Toast.makeText(applicationContext,"Address has been changed successfully ! ", Toast.LENGTH_LONG).show()
            }

        }
    }
    private fun checkAddress(address1 : String): Boolean?{
        if(TextUtils.isEmpty(address1)){
            addressArea.error = getString(R.string.error_field_required)
            focusView = addressArea
            cancel = true
            isAdressValid = false
        }
        else{
            cancel = false
            isAdressValid = true
        }
        if (cancel) {
            Toast.makeText(applicationContext,"Please enter a valid address ! ", Toast.LENGTH_LONG).show()
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isAdressValid
    }

}

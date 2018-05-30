package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    var user:User ?= null
    var newPassword1 : String ?= null
    var newPassword2 : String ?= null
    var oldPasswordEntered : String ?= null
    var oldPasswordX : String ?= null

    var isPassword1Valid : Boolean ?= null
    var isPassword2Valid : Boolean ?= null
    var isOldPasswordValid : Boolean ?= null

    var isOldPasswordEmpty : Boolean ?= null
    var isPassword1Empty : Boolean ?= null
    var isPassword2Empty : Boolean ?= null


    var isPasswordsEqual : Boolean ?= null



    var isEmpty : Boolean ?= null

    var isOk : Boolean ?=null

    var cancel = false
    var focusView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        oldPasswordText.error=null
        newPasswordText.error=null
        newPasswordText2.error=null

        reference= FirebaseDatabase.getInstance().getReference("users")

        user= intent.getSerializableExtra("User") as? User
        oldPasswordX = user!!.userPassword

        print("PASSWORD : ")
        println(oldPasswordX)

        changePassButton.setOnClickListener(){

            oldPasswordEntered = oldPasswordText.text.toString()
            newPassword1 = newPasswordText.text.toString()
            newPassword2 = newPasswordText2.text.toString()

            isOldPasswordEmpty = checkEmpty(oldPasswordEntered!!)

            if(isOldPasswordEmpty!!){

                oldPasswordText.error = getString(R.string.error_field_required)
                focusView = oldPasswordText
                focusView?.requestFocus()

            }
            isPassword1Empty = checkEmpty(newPassword1!!)

            if(isPassword1Empty!!){

                newPasswordText.error = getString(R.string.error_field_required)
                focusView = newPasswordText
                focusView?.requestFocus()

            }

            isPassword2Empty = checkEmpty(newPassword2!!)

            if(isPassword2Empty!!){

                newPasswordText2.error = getString(R.string.error_field_required)
                focusView = newPasswordText2
                focusView?.requestFocus()

            }

            isOldPasswordValid = checkOldPassword(oldPasswordEntered!!)

            if(!isOldPasswordValid!!){

                oldPasswordText.error = "You entered incorrect password!"
                focusView = oldPasswordText
                focusView?.requestFocus()

            }

            isPassword1Valid = checkPassword1(newPassword1!!)

            if(!isPassword1Valid!!){
                newPasswordText.error="Password is too short !"
                focusView = newPasswordText
                focusView?.requestFocus()

            }

            isPassword2Valid = checkPassword2(newPassword2!!)

            if(!isPassword2Valid!!){

                newPasswordText2.error="Password is too short !"
                focusView = newPasswordText2
                focusView?.requestFocus()

            }

            isPasswordsEqual = validatePasswords(newPassword1!!,newPassword2!!)

            if(!isPasswordsEqual!!){
                newPasswordText.error="Password doesn't match !"
                newPasswordText2.error="Password doesn't match !"

                focusView = newPasswordText2
                focusView?.requestFocus()

                focusView = newPasswordText
                focusView?.requestFocus()

            }
            if((!isOldPasswordEmpty!!)&&(!isPassword1Empty!!)&&(!isPassword2Empty!!)&&(isOldPasswordValid!!)
                    &&(isPassword1Valid!!)&&(isPassword2Valid!!)&&(isPasswordsEqual!!)){

                reference!!.child(user!!.userId).child("userPassword").setValue(newPassword1)

                Toast.makeText(applicationContext,"Your password has been changed successfully ! ", Toast.LENGTH_LONG).show()

                val returnSettingsActivity = Intent(this,MainActivity::class.java)
                returnSettingsActivity.putExtra("User",user)
                startActivity(returnSettingsActivity)
            }
        }
    }
    private fun checkEmpty(pass : String):Boolean{
        if(TextUtils.isEmpty(pass)){
            oldPasswordText.error = getString(R.string.error_field_required)
            isEmpty = true
        }
        else{

            isEmpty = false
        }
        return isEmpty!!
    }

    private fun checkOldPassword(pass : String):Boolean{

        if(pass.equals(oldPasswordX)){
            isOk = true
        }
        else{
            isOk = false
        }

        return isOk!!
    }
    private fun checkPassword1(pass : String):Boolean{

        if(pass.length >4){
            isOk = true
        }
        else{
            isOk = false
        }

        return isOk!!
    }
    private fun checkPassword2(pass : String):Boolean{

        if(pass.length >4){
            isOk = true
        }
        else{
            isOk = false
        }

        return isOk!!
    }
    private fun validatePasswords(pass1 : String,pass2 : String):Boolean{

        if(pass1.equals(pass2)){
            isOk = true
        }
        else{
            isOk = false
        }
        return isOk!!
    }
}

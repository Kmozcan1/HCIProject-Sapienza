package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_register.*
import android.text.TextUtils
import android.util.Log
import com.google.firebase.database.*

var selectedCountry: String ?= null
var selectedCity: String ?= null
var selectedZone: String ?= null

var firstname : String ?=null
var lastname : String ?=null
var email : String ?=null
var password1 : String ?=null
var password2 : String ?=null
var address1 : String ?= null
var userMobileNum : String ?= null

var cancel = false
var focusView: View? = null

var isMailValid : Boolean ?=null
var isNamesValid : Boolean ?= null
var isPasswordValid : Boolean ?= null
var isAdressValid : Boolean ?= null
var isNumValid : Boolean ?= null


var isCountryValid : Boolean ?= null
var isCityValid : Boolean ?= null
var isZoneValid : Boolean ?= null

var reference : DatabaseReference ?= null
var userList : MutableList<User> ?= null

var userAlreadyExist : Boolean ?= null


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        userAlreadyExist = false
        userList = mutableListOf()
        reference= FirebaseDatabase.getInstance().getReference("users")

        val countrySpinner = findViewById<Spinner>(R.id.countryListSpinner)

        val countryListAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,resources.getStringArray(R.array.countryNames))
        countryListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        countrySpinner.setAdapter(countryListAdapter)

        countryListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCountry = countryListSpinner.selectedItem.toString()

                if(selectedCountry!!.startsWith('-')){
                    onNothingSelected(countryListSpinner)
                }
                if (selectedCountry.equals("Italy")){
                    isCountryValid = true

                    showCitiesItaly()
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                focusView = countryListSpinner
                focusView?.requestFocus()
                isCountryValid = false

            }
        }
        register_submit_button.setOnClickListener {
            firstname = firstName.text.toString()
            lastname = lastName.text.toString()
            email = register_email.text.toString()
            password1 =password.text.toString()
            password2 =re_password.text.toString()
            address1= address.text.toString()
            userMobileNum =user_phone.text.toString()


            //RESET ERRORS
            firstName.error = null
            lastName.error = null
            register_email.error = null
            password.error = null
            re_password.error = null
            address.error = null
            user_phone.error = null

            if((checkEmail(email!!)==true)&&(checkNames(firstname!!, lastname!!)==true)
                    &&(checkPassword(password1!!,password2!!) ==true) &&(checkNum(userMobileNum!!)==true)
                    &&(checkAddress(address1!!)==true)&&(isCountryValid!!)
                    &&(isCityValid!!)&&(isZoneValid!!)&&(isNumValid!!)){
                println("Dataretrieve will be done ! ")

                reference!!.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                    override fun onDataChange(p0: DataSnapshot?) {

                        if(p0!!.exists()){

                            for(userObject in p0.children){
                                val usr = userObject.getValue(User :: class.java)
                                userList!!.add(usr!!)
                                println("Already registered e-mail addresses are :")
                                println(usr.email)
                                if(usr.email.equals(register_email.text.toString())){
                                    userAlreadyExist = true
                                }
                            }
                        }
                        else{
                            userAlreadyExist=false

                        }
                    }
                })
                if(userAlreadyExist!!){
                    register_email.error = "Please enter different e-mail address"
                    focusView = register_email
                    cancel = true
                    isMailValid = false
                    Toast.makeText(applicationContext,"This e-mail has been already registered ! ", Toast.LENGTH_LONG).show()

                }
                else{
                    finishRegistration(firstname!!,lastname!!,email!!,password1!!,userMobileNum!!,
                            address1!!,selectedCountry!!, selectedCity!!, selectedZone!!)
                }

            }
            else if (isCountryValid == false){
                Toast.makeText(applicationContext,"Please select your country ! ", Toast.LENGTH_LONG).show()
            }
            else if (isCityValid == false){
                Toast.makeText(applicationContext,"Please select your city ! ", Toast.LENGTH_LONG).show()

            }
            else if (isZoneValid == false){
                Toast.makeText(applicationContext,"Please select your zone ! ", Toast.LENGTH_LONG).show()

            }
        }

    }
    private fun finishRegistration(firstname : String,lastname : String,email : String,password1 : String,
                                   userMobileNum : String,address : String,selectedCountry :String,
                                   selectedCity :String, selectedZone :String){

        //CHECK IF USER ALREADY EXISTS OR NOT AND WRITE ALL INFO TO DATABASE

        val userObject :User = User(firstname,lastname,email,password1, userMobileNum,
                                    selectedCountry,selectedCity,selectedZone,address)

        val ref = FirebaseDatabase.getInstance().getReference("users")

        val userId = ref.push().key

        ref.child(userId).setValue(userObject).addOnCompleteListener{
            Toast.makeText(applicationContext,"You registered successfully ! ", Toast.LENGTH_LONG).show()
            directMainActivity(email)

        }
    }
    private fun directMainActivity(userEmail: String){
        val mainActivityIntent = Intent(this,MainActivity::class.java)
        mainActivityIntent.putExtra("userEmail",userEmail)
        startActivity(mainActivityIntent)
    }

    private fun checkEmail(email : String): Boolean? {

        if(TextUtils.isEmpty(email)){

            register_email.error = getString(R.string.error_field_required)
            focusView = register_email
            cancel = true
            isMailValid = false
        }
        if((!email.contains("@"))){
            register_email.error = "Enter valid email address !"
            focusView = register_email
            cancel = true
            isMailValid = false
        }

        else{
            cancel = false
            isMailValid = true
        }
        if (cancel) {

            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isMailValid

    }
    private fun checkNum(num : String): Boolean? {

        if(TextUtils.isEmpty(num)){

            user_phone.error = getString(R.string.error_field_required)
            focusView = user_phone
            cancel = true
            isNumValid = false
        }

        else{
            cancel = false
            isNumValid = true
        }
        if (cancel) {

            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isNumValid

    }

    private fun checkNames(firstname : String,lastname : String): Boolean?{
        if(TextUtils.isEmpty(firstname)){
            firstName.error = getString(R.string.error_field_required)
            focusView = firstName
            cancel = true
            isNamesValid = false
        }
        else{
            cancel = false
            isNamesValid = true
        }
        if(TextUtils.isEmpty(lastname)){
            lastName.error = getString(R.string.error_field_required)
            focusView = lastName
            cancel = true
            isNamesValid = false
        }
        else{
            cancel = false
            isNamesValid = true
        }
        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isNamesValid
    }
    private fun checkPassword(password1 : String,password2 : String): Boolean?{
        if(TextUtils.isEmpty(password1)){
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
            isPasswordValid = false
        }
        else{
            cancel = false
            isPasswordValid = true
        }
        if(password1.length <=4){
            password.error = "Password is too short !"
            focusView = password
            cancel = true
            isPasswordValid = false

        }
        else{
            cancel = false
            isPasswordValid = true
        }
        if(TextUtils.isEmpty(password2)){
            re_password.error = getString(R.string.error_field_required)
            focusView = re_password
            cancel = true
            isPasswordValid = false
        }
        else{
            cancel = false
            isPasswordValid = true
        }
        if(!password1.equals(password2)){
            re_password.error = "Passwords doesn't match !"
            focusView = re_password
            cancel = true
            isPasswordValid = false

        }
        else{
            cancel = false
            isPasswordValid = true
        }
        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isPasswordValid
    }
    private fun checkAddress(address1 : String): Boolean?{
        if(TextUtils.isEmpty(address1)){
            address.error = getString(R.string.error_field_required)
            focusView = address
            cancel = true
            isAdressValid = false
        }
        else{
            cancel = false
            isAdressValid = true
        }
        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView?.requestFocus()
        }

        return isAdressValid
    }


    fun showCitiesItaly(){
        val citySpinner = findViewById<Spinner>(R.id.cityListSpinner)
        val cityListAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,resources.getStringArray(R.array.cityNamesItaly))
        cityListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        citySpinner.setAdapter(cityListAdapter)

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCity = citySpinner.selectedItem.toString()

                if(selectedCity!!.startsWith('-')){
                    onNothingSelected(citySpinner)
                }
                if (selectedCountry.equals("Italy") && selectedCity.equals("Roma")){

                    isCityValid = true
                    showZoneInRoma()
                }
                if (selectedCity.equals("Milano")){

                    isCityValid = true
                    showZoneInMilano()
                }

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                focusView = citySpinner
                focusView?.requestFocus()
                isCityValid = false
            }
        }

    }
    private fun showZoneInRoma(){
        val zoneSpinner = findViewById<Spinner>(R.id.zoneListSpinner)
        val zoneListAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,resources.getStringArray(R.array.zoneNamesRoma))
        zoneListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        zoneSpinner.setAdapter(zoneListAdapter)

        zoneSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedZone = zoneSpinner.selectedItem.toString()

                if(selectedZone!!.startsWith('-')){
                    onNothingSelected(zoneSpinner)
                }
                else{
                    if(selectedCountry.equals("Italy") && selectedCity.equals("Roma")){
                        // ADD ZONE TO DATABASE

                        isZoneValid = true

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                focusView = zoneSpinner
                focusView?.requestFocus()
                isZoneValid = false


            }
        }

    }
    private fun showZoneInMilano(){

    }

}



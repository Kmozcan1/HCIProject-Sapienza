package com.example.onlinemarket.onlinemarket

/**
 * Created by EmreSelcuk on 24.4.2018.
 */
class User ( val firstName : String,val lastname : String,
            val email : String,val userPassword : String,val userPassword2 : String,
             val country : String,val city : String, val zone : String,val address : String){

    constructor():this ("","","","","","","","","")
}


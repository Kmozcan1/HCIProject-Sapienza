package com.example.onlinemarket.onlinemarket

import java.io.Serializable

/**
 * Created by EmreSelcuk on 24.4.2018.
 */
class User (val userId :String, val firstName : String, val lastname : String,
            val email : String, val userPassword : String, val userMobilePhone : String,
            val country : String, var city : String, val zone : String, var address : String) :Serializable {

    constructor():this ("","","","","","","","","","")

}


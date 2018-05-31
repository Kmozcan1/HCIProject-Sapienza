package com.example.onlinemarket.onlinemarket

import java.io.Serializable

/**
 * Created by EmreSelcuk on 24.4.2018.
 */
class User (var userId :String, var firstName : String, var lastname : String,
            var email : String, var userPassword : String, var userMobilePhone : String,
            var country : String, var city : String, var zone : String, var address : String) :Serializable {

    constructor():this ("","","","","","","","","","")

}


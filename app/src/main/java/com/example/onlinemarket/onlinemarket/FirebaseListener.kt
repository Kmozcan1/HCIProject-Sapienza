package com.example.onlinemarket.onlinemarket

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

interface FireBaseListener {
    fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference)
}
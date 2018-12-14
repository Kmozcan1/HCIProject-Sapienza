package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {

    var preferences : SharedPreferences?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        insert_product_button.setOnClickListener {
            intent = Intent(this,InsertProductActivity::class.java)
            startActivity(intent)
        }
        edit_product_button.setOnClickListener {
            intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }
        insert_company_button.setOnClickListener {
            intent = Intent(this, InsertCompanyActivity::class.java)
            startActivity(intent)
        }
        edit_company_button.setOnClickListener {
            intent = Intent(this, CompanyListActivity::class.java)
            startActivity(intent)
        }
        order_history_button.setOnClickListener {
            intent = Intent(this, OrderListActivity::class.java)
            startActivity(intent)
        }
        logout_button.setOnClickListener {
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            val editor: SharedPreferences.Editor = preferences!!.edit()
            editor.remove("isLogged")
            editor.commit()
            intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        val editor: SharedPreferences.Editor = preferences!!.edit()
        editor.putInt("isLogged",1)
        editor.putString("email", Utilities.activeUser!!.email)
        editor.commit()

        val dNow = Date()
        val ft = SimpleDateFormat("yyMMddhhmmssMs")
        val datetime = ft.format(dNow)
        if(Utilities.activeUser!!.email == "conad@conad" ||
                Utilities.activeUser!!.email == "carrefour@carrefour") {
            insert_company_button.visibility = View.GONE
            edit_company_button.visibility = View.GONE
        }

    }

    override fun onBackPressed() {
        val homeIntent =  Intent(Intent.ACTION_MAIN, null)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        startActivity(homeIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_product, menu)
        return true
    }
}
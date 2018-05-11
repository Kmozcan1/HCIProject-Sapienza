package com.example.onlinemarket.onlinemarket

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.onlinemarket.onlinemarket.R.id.drawer_layout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.android.gms.internal.tv
import android.widget.AdapterView



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var FBdatabase : DatabaseReference?= null
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val emailText = nav_view.getHeaderView(0).findViewById<TextView>(R.id.e_mail_label)
        emailText.text= intent.getSerializableExtra("userEmail").toString()

        val companyList = ArrayList<Company>()
        val companyListView = findViewById<ListView>(R.id.company_list_view)


        FBdatabase= FirebaseDatabase.getInstance().getReference("companies")
        FBdatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                companyList.clear()
                if(p0!!.exists()) {
                    for (cmpObject in p0.children) {
                        val name= cmpObject.child("companyName").getValue(String::class.java)
                        val image= cmpObject.child("image").getValue(String::class.java)
                        val openTime= cmpObject.child("openTime").getValue(String::class.java)
                        val closeTime=cmpObject.child("closeTime").getValue(String::class.java)
                        val cmp :Company= Company(name ,image ,openTime ,closeTime)
                        companyList.add(cmp)
                        val companyAdapter = CompanyListViewAdapter(baseContext, companyList)
                        companyListView.adapter = companyAdapter

                    }
                }
            }
        })
        val productIntent = Intent(this,ProductActivity::class.java)
        companyListView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item text from ListView
                val selectedItem = parent.getItemAtPosition(position) as Company
                startActivity(productIntent)
            }
        };

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

package com.example.onlinemarket.onlinemarket

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.onlinemarket.onlinemarket.R.id.drawer_layout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.android.gms.internal.tv
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_company_list.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    var preferences : SharedPreferences?= null
    var FBdatabase : DatabaseReference?= null
    private lateinit var mMap: GoogleMap
    var user:User ?= null
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val order :Order
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utilities.handleProgressBarAction(contentMain_progressBar, window, true)
        setSupportActionBar(toolbar)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment!!.getMapAsync(this)
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val editor:Editor = preferences!!.edit();
        editor.putInt("isLogged",1)
        editor.commit();


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val navEmailText = nav_view.getHeaderView(0).findViewById<TextView>(R.id.e_mail_label)
        val navNameText = nav_view.getHeaderView(0).findViewById<TextView>(R.id.name_label)
        val navAddressText = nav_view.getHeaderView(0).findViewById<TextView>(R.id.address_label)
        val navCityText = nav_view.getHeaderView(0).findViewById<TextView>(R.id.city_label)
        val navAdminButton= nav_view.menu.getItem(0)
        user= intent.getSerializableExtra("User") as? User
        val FBuserDatabase= FirebaseDatabase.getInstance().getReference("user")
        FBuserDatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    for (usr in p0.children) {
                        if(usr.child("email").getValue() == user!!.email) {
                            user!!.address= usr.child("address").getValue() as String
                            user!!.city= usr.child("city").getValue() as String
                            navAddressText.text= user!!.address
                            navCityText.text=user!!.city
                        }
                    }
                }
            }

        })

        navEmailText.text=  user!!.email
        navAddressText.text= user!!.address
        navCityText.text=user!!.city
        if(navEmailText.text == "admin@admin")
            navAdminButton.setVisible(true)
        navNameText.text= user!!.firstName + " " + user!!.lastname

        val companyList = ArrayList<Company>()
        val companyListView = findViewById<ListView>(R.id.company_list_view)

        order= Order(user!!.email,user!!.address,user!!.zone,"")


        FBdatabase= FirebaseDatabase.getInstance().getReference("companies")

        FBdatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                companyList.clear()
                if(p0!!.exists()) {
                    for (cmpObject in p0.children) {
                        val name = cmpObject.child("companyName").getValue(String::class.java)
                        val image = cmpObject.child("image").getValue(String::class.java)
                        val openTime = cmpObject.child("openTime").getValue(String::class.java)
                        val closeTime = cmpObject.child("closeTime").getValue(String::class.java)
                        val cmp = Company(name, image, openTime, closeTime)
                        companyList.add(cmp)
                    }

                    val companyAdapter = CompanyListViewAdapter(baseContext, companyList)
                    companyListView.adapter = companyAdapter
                    FBdatabase!!.removeEventListener(this)
                }
                Utilities.handleProgressBarAction(contentMain_progressBar, window, false)
            }
        })
        val productIntent = Intent(this,ProductActivity::class.java)
        companyListView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item text from ListView
                val selectedItem = parent.getItemAtPosition(position) as Company
                if(selectedItem.calculateOpenOrClosed()=="Open") {
                    productIntent.putExtra("companyName", selectedItem.companyName)
                    productIntent.putExtra("user", user)
                    productIntent.putExtra("order", order)
                    startActivity(productIntent)
                }
                else
                {
                    val alert = AlertDialog.Builder(this@MainActivity)
                    alert.setTitle("Alert")
                    alert.setMessage("This company is now closed!")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                }
            }
        }


    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val conad = LatLng(41.919742, 12.522352)
        mMap.addMarker(MarkerOptions().position(conad).title("Conad"))

        val carre = LatLng(41.919296, 12.520331)

        val between =  LatLng((carre.latitude+ conad.latitude)/2 , (carre.longitude+ conad.longitude)/2)
        mMap.addMarker(MarkerOptions().position(carre).title("Carrefour"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((between), 15.0f))
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
        val adminActivity = Intent(this,AdminActivity::class.java)
        val settingsActivity = Intent(this,SettingsActivity::class.java)
        when (item.itemId) {

            R.id.nav_Admin -> {
                startActivity(adminActivity)
            }
            R.id.nav_manage -> {
                settingsActivity.putExtra("user", user)
                startActivity(settingsActivity)

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

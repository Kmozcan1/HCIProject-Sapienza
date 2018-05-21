package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_company_list.*

private var companiesListener: ValueEventListener? = null
private var companiesQuery: DatabaseReference? = null

class CompanyListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_list)

        //Make the screen non-interactive & progressBar visible
        Utilities.handleProgressBarAction(companyList_progressBar, window, true)

        //Get companies
        Utilities.getCompanies(object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                val companies = value as ArrayList<Company?>
                companiesListener = listener
                companiesQuery = query
                val companyAdapter = CompanyListViewAdapter(baseContext, companies)
                admin_company_list_view.adapter = companyAdapter
                Utilities.handleProgressBarAction(companyList_progressBar, window, false)
            }
        })

        admin_company_list_view.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val selectedItem = admin_company_list_view.getItemAtPosition(position) as Company
            val intent = Intent(this@CompanyListActivity, InsertCompanyActivity::class.java)
            intent.putExtra("from", "companyList")
            intent.putExtra("companyKey", selectedItem.companyKey)
            startActivity(intent)
        }
    }



    //Remove the event listener upon exit
    override fun onDestroy() {
        super.onDestroy()
        companiesQuery!!.removeEventListener(companiesListener)
    }
}
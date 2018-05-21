package com.example.onlinemarket.onlinemarket

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_product_list.*
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.view.Menu
import android.widget.AdapterView
import android.widget.SearchView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_insert_product.*

private var productListItemList = mutableListOf<ProductListItemData>()
private var companiesListener: ValueEventListener? = null
private var productsListener: ValueEventListener? = null
private var companiesQuery: DatabaseReference? = null
private var productsQuery: DatabaseReference? = null

@RequiresApi(api = Build.VERSION_CODES.O)
class ProductListActivity : AppCompatActivity(), DialogFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        //Make the screen non-interactive & progressBar visible
        Utilities.handleProgressBarAction(productList_progressBar, window, true)

        //Fetches and maps the company images to their names
        //Used in pairing the product with its companyImage in ProductListItemData
        //Then calls fillProductSearchList
        val companyMap = mutableMapOf<String, String>()
        Utilities.getCompanies(object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                val companies = value as ArrayList<Company?>
                companiesListener = listener
                companiesQuery = query
                for (company in companies) {
                    companyMap[company!!.companyName] = company!!.image
                }
                fillProductSearchList(companyMap)
            }
        })
        product_listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->

            val newFragment = ProductDetailsDialogFragment()

            //Make the activity untouchable to prevent user from selecting another list item
            //Because android has a bug that makes listview button process on delay amk.
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            val manager = this@ProductListActivity.fragmentManager
            val ft = manager.beginTransaction()
            val prev = manager.findFragmentByTag("details")
            if (prev != null) {
                ft.remove(prev)
            }

            newFragment.newInstance(product_listView.getItemAtPosition(position) as ProductListItemData)
            newFragment.show(ft, "details")
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            applySearchFilter(productListItemList, query)
        }
    }





    //Fetches the products, pairs them with company images
    //and fills the listView
    private fun fillProductSearchList(companyMap: MutableMap<String, String>) {
        Utilities.getProducts(object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                productListItemList.clear()
                productsListener = listener
                productsQuery = query
                val products = value as ArrayList<Product?>
                for (product in products) {
                    val productListItem =
                            ProductListItemData(product!!.productKey, product!!.productName,
                            product!!.productImage, product!!.price,
                                    product!!.company, companyMap[product!!.company])
                    productListItemList.add(productListItem)
                }
                val productSearchListViewAdapter = ProductSearchListViewAdapter(baseContext,
                        R.layout.productsearch_list_item, productListItemList)
                product_listView.adapter = productSearchListViewAdapter

                //Turn progressBar visible and screen touchable
                Utilities.handleProgressBarAction(productList_progressBar, window, false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_listproduct, menu)
        val searchMenuItem = menu.findItem(R.id.search) // get my MenuItem with placeholder submenu
        searchMenuItem.expandActionView() // Expand the search menu item in order to show by default the query
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            //TODO: Try to make this async
            override fun onQueryTextChange(newText: String): Boolean {
                if(newText == "") {
                    val productSearchListViewAdapter = ProductSearchListViewAdapter(baseContext,
                            R.layout.productsearch_list_item, productListItemList)
                    product_listView.adapter = productSearchListViewAdapter

                   /* product_listView.onItemClickListener = object : AdapterView.OnItemClickListener {
                        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val newFragment = ProductDetailsDialogFragment()
                            val fm = this@ProductListActivity.fragmentManager
                            newFragment.show(fm, "missiles")
                        }
                    }*/



                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                //Task HERE
                return false
            }

        })

        return true
    }

    //Applies the filter and changes the adapter list
    private fun applySearchFilter(list: MutableList<ProductListItemData>, query: String) {
        if (query != "") {
            val filteredList = mutableListOf<ProductListItemData>()
            for (item in list) {
                if (item.productName.toLowerCase().contains(query.toLowerCase()) || item.companyName.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item)
                }
            }
            val productSearchListViewAdapter = ProductSearchListViewAdapter(baseContext,
                    R.layout.productsearch_list_item, filteredList)
            product_listView.adapter = productSearchListViewAdapter
        }
    }

    //upon closing the fragment, make the activity touchable again
    override fun handleDialogClose(dialog: DialogInterface) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        productsQuery!!.removeEventListener(productsListener)
        companiesQuery!!.removeEventListener(companiesListener)
    }
}
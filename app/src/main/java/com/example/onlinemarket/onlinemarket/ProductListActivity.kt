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
import android.view.Menu
import android.widget.SearchView

private var productListItemList = mutableListOf<ProductListItemData>()

@RequiresApi(api = Build.VERSION_CODES.O)
class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)



        //Make the screen non-interactive & progressBar visible
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = View.VISIBLE

        //Fetches and maps the company images to their names
        //Used in pairing the product with its companyImage in ProductListItemData
        //Then calls fillProductSearchList
        val companyMap = mutableMapOf<String, String>()
        Utilities.getCompanies(object: FireBaseListener {
            override fun onCallBack(value: Any) {
                val companies = value as ArrayList<Company?>
                for (company in companies) {
                    companyMap[company!!.companyName] = company!!.image
                }
                fillProductSearchList(companyMap)
            }
        })
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
            override fun onCallBack(value: Any) {
                productListItemList.clear()
                val products = value as ArrayList<Product?>
                for (product in products) {
                    val productListItem = ProductListItemData(product!!.productName,
                            product!!.productImage, product.company, companyMap[product!!.company])
                    productListItemList.add(productListItem)
                }
                val productSearchListViewAdapter = ProductSearchListViewAdapter(baseContext,
                        R.layout.productsearch_list_item, productListItemList)
                product_listView.adapter = productSearchListViewAdapter

                //Turn progressBar visible and screen touchable
                progressBar.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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


}
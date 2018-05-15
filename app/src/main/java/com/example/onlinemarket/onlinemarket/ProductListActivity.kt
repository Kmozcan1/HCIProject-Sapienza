package com.example.onlinemarket.onlinemarket

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.onlinemarket.onlinemarket.R.id.product_listView
import kotlinx.android.synthetic.main.activity_product_list.*

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



    //Fetches the products, pairs them with company images
    //and fills the listView
    private fun fillProductSearchList(companyMap: MutableMap<String, String>) {
        val productListItemList = mutableListOf<ProductListItemData>()
        Utilities.getProducts(object: FireBaseListener {
            override fun onCallBack(value: Any) {
                val products = value as ArrayList<Product?>
                for (product in products) {
                    val productListItem = ProductListItemData(product!!.productName,
                            product!!.productImage, companyMap[product!!.company])
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

/*data class ProductListItemData(val productID: String, val ProductName: String,
                               val productImage: String, var companyImage: String)*/
}
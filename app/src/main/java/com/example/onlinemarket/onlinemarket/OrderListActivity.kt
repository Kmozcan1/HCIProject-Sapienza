package com.example.onlinemarket.onlinemarket

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.example.onlinemarket.onlinemarket.R.id.order
import com.example.onlinemarket.onlinemarket.R.string.companyName
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.fragment_order_list.*
import java.util.*

private var orderListtItemList = mutableListOf<Order>()
private var companiesListener: ValueEventListener? = null
private var ordersListener: ValueEventListener? = null
private var companiesQuery: DatabaseReference? = null
private var ordersQuery: DatabaseReference? = null
private const val NO_ACTIVE_ORDERS_FOUND_MESSAGE = "No active orders have been found"
private const val NO_ORDERS_FOUND_MESSAGE = "No orders have been found"

/**
 * The [android.support.v4.view.PagerAdapter] that will provide
 * fragments for each of the sections. We use a
 * [FragmentPagerAdapter] derivative, which will keep every
 * loaded fragment in memory. If this becomes too memory intensive, it
 * may be best to switch to a
 * [android.support.v4.app.FragmentStatePagerAdapter].
 */
//private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

/**
 * The [ViewPager] that will host the section contents.
 */
//private var mViewPager: ViewPager? = null

@RequiresApi(api = Build.VERSION_CODES.O)
class OrderListActivity : AppCompatActivity(), DialogFragmentListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        Utilities.handleProgressBarAction(orderList_progressBar, window, true)
        val tabLayout = findViewById<TabLayout>(R.id.orderTabs)
        /*if (Utilities.activeUser!!.email == "admin@admin") {
            (tabLayout.getChildAt(0) as ViewGroup).getChildAt(1).visibility = View.GONE
        }*/

        //Make the screen non-interactive & progressBar visible
        val companyMap = mutableMapOf<String, String>()
        Utilities.getCompanies(object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                val companies = value as ArrayList<Company?>
                companiesListener = listener
                companiesQuery = query
                for (company in companies) {
                    companyMap[company!!.companyName] = company!!.image
                }
                fillOrderList(companyMap)
            }
        })



        //val ICONS = intArrayOf(R.drawable.food, R.drawable.grocery, R.drawable.meat, R.drawable.frozen, R.drawable.dairy, R.drawable.drink, R.drawable.alcohol, R.drawable.personalcare, R.drawable.cleaning)

        //tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        /*for (i in 0..8)
            tabLayout.getTabAt(i)!!.setIcon(ICONS[i])*/

    }

    private fun fillOrderList(companyMap: MutableMap<String, String>) {
        Utilities.getOrders(Utilities.activeUser!!.email, object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                orderListtItemList.clear()
                ordersListener = listener
                ordersQuery = query
                val orders = value as ArrayList<Order?>

                /* public Order(String orderKey, Boolean isDone, String userEmail, String address, String zone,
                 String companyName, Double totalPrice, String companyImage, HashMap<Product,Integer> products, String time) {*/
                for (order in orders) {
                    val orderListItem =
                            Order(order!!.orderKey, order!!.done, order!!.userEmail,
                                    order!!.address, order!!.zone, order!!.companyName,
                                    order!!.totalPrice, companyMap[order!!.companyName],
                                    order!!.orderedProducts, order.time, order!!.orderNo)
                    orderListtItemList.add(orderListItem)
                }

                orderListtItemList = orderListtItemList.asReversed()

                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

                // Set up the ViewPager with the sections adapter.
                val mViewPager = findViewById<ViewPager>(R.id.order_container)
                mViewPager.adapter = mSectionsPagerAdapter
                //mViewPager.setOffscreenPageLimit(10)

                val tabLayout = findViewById<TabLayout>(R.id.orderTabs)

                mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
                tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))
                //Turn progressBar visible and screen touchable
                Utilities.handleProgressBarAction(orderList_progressBar, window, false)
            }
        })
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_order_list)

        order_listView.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->

            val newFragment = OrderDetailsDialogFragment()

            //Make the activity untouchable to prevent user from selecting another list item
            //Because android has a bug that makes listview button process on delay amk.
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            val manager = this@OrderListActivity.fragmentManager
            val ft = manager.beginTransaction()
            val prev = manager.findFragmentByTag("details")
            if (prev != null) {
                ft.remove(prev)
            }

            newFragment.newInstance(order_listView.getItemAtPosition(position) as Order)
            newFragment.show(ft, "details")
        }


    }*/


    override fun handleDialogClose(dialog: DialogInterface) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 9 total pages.
            return 2
        }
    }


    class PlaceholderFragment : Fragment() {


        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {

            val rootView = inflater!!.inflate(R.layout.fragment_order_list, container, false)
            val listView = rootView.findViewById<ListView>(R.id.order_listView)
            val emptyView = rootView.findViewById<TextView>(R.id.noOrdersFound_textView)
            listView.emptyView = emptyView
            val section = arguments.getInt(ARG_SECTION_NUMBER)
            var sectionList = mutableListOf<Order>()
            if (section == 1) {
                emptyView.text = NO_ACTIVE_ORDERS_FOUND_MESSAGE
                for (order in orderListtItemList) {
                    if (!order.done) {
                        sectionList.add(order)
                    }
                }
            }
            else {
                emptyView.text = NO_ORDERS_FOUND_MESSAGE
                for (order in orderListtItemList) {
                    if (order.done) {
                        sectionList.add(order)
                    }
                }
            }


            val orderListViewAdapter = OrderListViewAdapter(activity.applicationContext,
                    R.layout.orderhistory_list_item, sectionList)
            listView.adapter = orderListViewAdapter
            listView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->

                val newFragment = OrderDetailsDialogFragment()

                //Make the activity untouchable to prevent user from selecting another list item
                //Because android has a bug that makes listview button process on delay amk.
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                val manager = activity.fragmentManager
                val ft = manager.beginTransaction()
                val prev = manager.findFragmentByTag("details")
                if (prev != null) {
                    ft.remove(prev)
                }

                newFragment.newInstance(order_listView.getItemAtPosition(position) as Order)
                newFragment.show(ft, "details")
            }
            return rootView
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ordersQuery!!.removeEventListener(ordersListener)
        companiesQuery!!.removeEventListener(companiesListener)
    }

}
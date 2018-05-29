package com.example.onlinemarket.onlinemarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.TimeUnit;

public class ProductActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public String companyName;
    public User user;
    public Order order;
    public Double TotalPrice;

    @Override
    public void onBackPressed() {

        if(!order.getProducts().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.alert_goback)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ProductActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else
            ProductActivity.this.finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        companyName = getIntent().getSerializableExtra("companyName").toString();
        user = (User) getIntent().getSerializableExtra("user");
        order= (Order) getIntent().getSerializableExtra("order");

        TotalPrice= 0.0;

        order.setDone(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(10);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        final Intent orderPage = new Intent(this, OrderActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order.getProducts().isEmpty()) {
                    orderPage.putExtra("user", user);
                    order.setTime();
                    order.setCompanyName(companyName);
                    //orderPage.putExtra("order", order);
                    Utilities.Companion.setOrder(order);
                    startActivity(orderPage);
                }
                else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProductActivity.this);
                    alert.setTitle("Alert");
                    alert.setMessage(R.string.alert_chooseProduct);
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }

            }
        });
        final int[] ICONS = new int[]{
                R.drawable.food,
                R.drawable.grocery,
                R.drawable.meat,
                R.drawable.frozen,
                R.drawable.dairy,
                R.drawable.drink,
                R.drawable.alcohol,
                R.drawable.personalcare,
                R.drawable.cleaning};

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for(int i=0; i<9; i++)
            tabLayout.getTabAt(i).setIcon(ICONS[i]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_COMPANY_NAME = "company_name";
        private static final String ARG_ORDER = "order";
        public ArrayList<Product> allProducts;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String companyName, Order order) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_COMPANY_NAME, companyName);
            args.putSerializable(ARG_ORDER, order);
            fragment.setArguments(args);
            return fragment;
        }
        public String getCategoryName(int tabID){
            String[] names = {"Food", "Grocery","Meat", "Frozen", "Dairy", "Drink", "Alcohol", "Personal Care", "Cleaning"};
            return names[tabID];
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            final View rootView = inflater.inflate(R.layout.fragment_product, container, false);


            TextView textView = rootView.findViewById(R.id.section_label);
            ImageView imageView= rootView.findViewById(R.id.section_image);

            final String companyName = getArguments().getString(ARG_COMPANY_NAME);
            final int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER)-1;
            final Order order= (Order) getArguments().getSerializable(ARG_ORDER);
            final ArrayList<Product> productList = new ArrayList<>();
            int [] Labels= new int[]{
                    R.string.tab_text_1,
                    R.string.tab_text_2,
                    R.string.tab_text_3,
                    R.string.tab_text_4,
                    R.string.tab_text_5,
                    R.string.tab_text_6,
                    R.string.tab_text_7,
                    R.string.tab_text_8,
                    R.string.tab_text_9
            };
            textView.setText(getString(Labels[sectionNumber]));
            final int[] ICONS = new int[]{
                    R.drawable.food,
                    R.drawable.grocery,
                    R.drawable.meat,
                    R.drawable.frozen,
                    R.drawable.dairy,
                    R.drawable.drink,
                    R.drawable.alcohol,
                    R.drawable.personalcare,
                    R.drawable.cleaning};
            imageView.setImageResource(ICONS[sectionNumber]);

            final GridView gridView=  rootView.findViewById(R.id.productGridView);
            final TextView priceText= getActivity().findViewById(R.id.priceText);
            final DatabaseReference query=  FirebaseDatabase.getInstance().getReference("products");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot productObject : dataSnapshot.getChildren()) {
                        if (companyName.equals(productObject.child("company").getValue()))
                            if(productObject.child("category").getValue().equals(getCategoryName(sectionNumber))){
                                String key = productObject.getKey();
                                String p_name = productObject.child("productName").getValue().toString();
                                String p_image = productObject.child("productImage").getValue().toString();
                                Double p_price = (Double) productObject.child("price").getValue();
                                String p_category = productObject.child("category").getValue().toString();
                                Product product = new Product(key ,p_name, p_price, companyName, p_image, p_category);
                                productList.add(product);
                            }
                    }
                    GridAdapter gridAdapter = new GridAdapter(getActivity().getApplicationContext(), productList, priceText, sectionNumber,9,order);
                    gridView.setAdapter(gridAdapter);
                    query.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*Utilities.Companion.getProducts(new FireBaseListener() {
                @Override
                public void onCallBack(@NotNull Object value) {
                    allProducts = (ArrayList<Product>) value;
                    for (Product productObject : allProducts) {
                        if (companyName.equals(productObject.company))
                            if(productObject.category.equals(getCategoryName(sectionNumber))){
                            String p_name = productObject.productName;
                            String p_image = productObject.productImage;
                            Double p_price = productObject.price;
                            String p_category = productObject.category;
                            Product product = new Product(p_name, p_price, companyName, p_image, p_category);
                            productList.add(product);
                        }
                    }
                    Utilities.Companion.getDatabase().getReference().removeEventListener();
                    GridAdapter gridAdapter = new GridAdapter(getActivity().getApplicationContext(), productList, priceText, sectionNumber,5);
                    gridView.setAdapter(gridAdapter);
                }
            });*/


            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, companyName,order);
        }

        @Override
        public int getCount() {
            // Show 9 total pages.
            return 9;
        }
    }
}

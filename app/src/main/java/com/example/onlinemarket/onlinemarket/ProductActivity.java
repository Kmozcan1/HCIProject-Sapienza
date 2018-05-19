package com.example.onlinemarket.onlinemarket;

import android.app.Activity;
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

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    public Double TotalPrice;
    public ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        companyName = getIntent().getSerializableExtra("companyName").toString();
        TotalPrice= 0.0;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final int[] ICONS = new int[]{
                R.drawable.food,
                R.drawable.grocery,
                R.drawable.drink,
                R.drawable.personalcare,
                R.drawable.cleaning};

        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);
        tabLayout.getTabAt(4).setIcon(ICONS[4]);

        productList= new ArrayList<>();
        final DatabaseReference FBdatabase = FirebaseDatabase.getInstance().getReference("products");
        FBdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot productObject : dataSnapshot.getChildren()) {
                    if (companyName.equals(productObject.child("company").getValue())) {
                        String p_name = (String) productObject.child("productName").getValue();
                        String p_image = (String) productObject.child("productImage").getValue();
                        Double p_price = (Double) productObject.child("price").getValue();
                        String p_category = (String) productObject.child("category").getValue();
                        Product product = new Product(p_name, p_price, companyName, p_image, p_category);
                        productList.add(product);
                    }
                }
                //FBdatabase.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        private static final String ARG_PRODUCT_LIST = "product_list";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String companyName, ArrayList<Product> productList) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_COMPANY_NAME, companyName);
            args.putSerializable(ARG_PRODUCT_LIST,productList);
            fragment.setArguments(args);
            return fragment;
        }
        public String getCategoryName(int tabID){
            String[] names = {"Food", "Grocery", "Drink", "PersonalCare", "Cleaning"};
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
            final ArrayList<Product> productList =(ArrayList<Product>) getArguments().getSerializable(ARG_PRODUCT_LIST);
            int [] Labels= new int[]{
                    R.string.tab_text_1,
                    R.string.tab_text_2,
                    R.string.tab_text_3,
                    R.string.tab_text_4,
                    R.string.tab_text_5
            };
            textView.setText(getString(Labels[sectionNumber]));
            final int[] ICONS = new int[]{
                    R.drawable.food,
                    R.drawable.grocery,
                    R.drawable.drink,
                    R.drawable.personalcare,
                    R.drawable.cleaning};
            imageView.setImageResource(ICONS[sectionNumber]);


            
            final GridView gridView=  rootView.findViewById(R.id.productGridView);
            final TextView priceText= getActivity().findViewById(R.id.priceText);



           ArrayList <Product> categorizedList= new ArrayList<>();
           if(productList!=null)
               for (Product productObject:productList) {
                    if(productObject.category.equals(getCategoryName(sectionNumber))) {
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);
                        categorizedList.add(productObject);

                    }
                }
           GridAdapter gridAdapter = new GridAdapter(getActivity().getApplicationContext(),categorizedList,priceText );
           
           gridView.setScrollingCacheEnabled(false);
           gridView.setAnimationCacheEnabled(false);
           gridView.setAdapter(gridAdapter);

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
            return PlaceholderFragment.newInstance(position + 1, companyName, productList);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }
}

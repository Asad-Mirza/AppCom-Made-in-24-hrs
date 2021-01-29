package com.example.admin.appcom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.Profile;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements list_frag.OnFragmentInteractionListener,form_frag.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener {
    private  TextView name,email;
   private   ImageView profileImageView;
  private sharedPrefData sharedPrefDataOB;
  private userPOJO userPOJOOB;


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPrefDataOB = new sharedPrefData();
        userPOJOOB = sharedPrefDataOB.getUserData(MainActivity.this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),2,MainActivity.this);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((SectionsPagerAdapter)mViewPager.getAdapter()).getFragment(position);

                if (position ==1 && fragment != null)
                {
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(mViewPager);
        mSectionsPagerAdapter.notifyDataSetChanged();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
         /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        name = (TextView) header.findViewById(R.id.name);

        profileImageView = header.findViewById(R.id.imageView);
        name.setText(userPOJOOB.getName());



        Glide.with(MainActivity.this)

                .load(userPOJOOB.getImageURL())
                .into(profileImageView);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        tabLayout = (TabLayout) findViewById(R.id.tablayout);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this,About_us_activity.class));
        } else if (id == R.id.nav_location) {
            startActivity(new Intent(MainActivity.this,LocationActivity.class));
        } else if (id == R.id.nav_list) {
            startActivity(new Intent(MainActivity.this,contact_list_activity.class));

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();

        } else if (id == R.id.nav_logout) {
            sharedPrefDataOB.logOutUser(MainActivity.this);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int noOFTabs;
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;
    Context mContext;
        public SectionsPagerAdapter(FragmentManager fm , int noOFTabs, Context mContext)

        { super(fm);
            this.mContext = mContext;

            this.noOFTabs = noOFTabs;
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //  return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0 :{
                   return Fragment.instantiate(mContext, form_frag.class.getName(), null);


                }
                case 1 :{
                    return Fragment.instantiate(mContext, list_frag.class.getName(), null);



                }


                }


            return null;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }



        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return  "ADD";
                case 1:
                    return "LIST";


                default:
                    return null;

            }

        }


        @Override
        public int getCount() {

            return 2;
        }
    }
}



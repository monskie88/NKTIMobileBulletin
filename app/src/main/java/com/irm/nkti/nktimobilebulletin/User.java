package com.irm.nkti.nktimobilebulletin;

import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.com.adapter.AdminListAdapter;
import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.navdrawer.SimpleSideDrawer;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseUser;


public class User extends ActionBarActivity {
    SimpleSideDrawer mNav;
    ActionBar actionBar;
    ParseImageView imgProfile;
    Button btnLogOut;
    Button btnAccount;
    Button btnFeed;
    Button btnUsers;
    Button btnPrefs;
    Button btnAbout;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);




        mNav=new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Users");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnAccount=(Button)findViewById(R.id.btnAccount);
        TextView txtName=(TextView)findViewById(R.id.txtName);
        txtName.setText(MainActivity.fullname);
        btnFeed=(Button)findViewById(R.id.btnFeed);
        btnUsers=(Button)findViewById(R.id.btnUsers);
        btnUsers.setTextColor(Color.GRAY);
        btnPrefs=(Button)findViewById(R.id.btnPref);
        btnAbout=(Button)findViewById(R.id.btnAbout);

        imgProfile=(ParseImageView)findViewById(R.id.imgProfilePhoto);
        imgProfile.setParseFile(MainActivity.imgFile);
        imgProfile.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

            }
        });



        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this, MainActivity.class));
            }
        });
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
                finish();
            }
        });

        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this,Preferences.class));
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User.this, LogIn.class));
                ParseUser.logOut();

                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(User.this);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("status","inactive");
                editor.apply();
                finish();


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                mNav.toggleLeftDrawer();
                break;
        }

        return super.onOptionsItemSelected(item);
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
            switch(position){
                case 0:{
                    return new Fragment1();
                }
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class Fragment1 extends Fragment {


        public Fragment1() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
            View rootView  = inflater.inflate(R.layout.fragment_user, container, false);
            ListView listAdmin=(ListView)rootView.findViewById(R.id.listAdmin);
            AdminListAdapter adapter=new AdminListAdapter(getActivity().getApplicationContext());
            listAdmin.setAdapter(adapter);
            return rootView;






        }
    }

    public static class Fragment2 extends Fragment {


        public Fragment2() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView  = inflater.inflate(R.layout.fragment_admin, container, false);
            return rootView;






        }
    }

    public static class Fragment3 extends Fragment {


        public Fragment3() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView  = inflater.inflate(R.layout.fragment_vip, container, false);
            return rootView;






        }
    }
}



package com.irm.nkti.nktimobilebulletin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;
import com.parse.ParseImageView;
import com.parse.ParseUser;

//This is a test i know
public class About extends ActionBarActivity {
    SimpleSideDrawer mNav;
    ActionBar actionBar;
    public static ParseImageView imgProfile;
    Button btnLogOut;
    Button btnNewPost;
    Button btnAccount;
    Button btnFeed;
    Button btnUsers;
    Button btnPrefs;
    Button btnAbout;
    RelativeLayout feedContainer;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mNav=new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar=getSupportActionBar();
        actionBar.setTitle("About");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnAccount=(Button)findViewById(R.id.btnAccount);
        txtName=(TextView)findViewById(R.id.txtName);
        btnFeed=(Button)findViewById(R.id.btnFeed);
        btnUsers=(Button)findViewById(R.id.btnUsers);
        btnPrefs=(Button)findViewById(R.id.btnPref);
        btnAbout=(Button)findViewById(R.id.btnAbout);
        btnAbout.setTextColor(Color.GRAY);

        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
                finish();
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileViewer.class));
                finish();
            }
        });
        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Preferences.class));
                finish();
            }
        });


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("status","inactive");
                editor.apply();
                finish();
                startActivity(new Intent(getApplicationContext(), LogIn.class));


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
}

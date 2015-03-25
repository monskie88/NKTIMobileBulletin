package com.irm.nkti.nktimobilebulletin;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.navdrawer.SimpleSideDrawer;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Preferences extends ActionBarActivity {
    SimpleSideDrawer mNav;
    android.support.v7.app.ActionBar actionBar;
    ParseImageView imgProfile;
    Button btnLogOut;
    Button btnAccount;
    Button btnFeed;
    Button btnUsers;
    Button btnPrefs;
    Button btnAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);


        mNav=new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Preferences");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnAccount=(Button)findViewById(R.id.btnAccount);
        TextView txtName=(TextView)findViewById(R.id.txtName);
        btnFeed=(Button)findViewById(R.id.btnFeed);

        btnUsers=(Button)findViewById(R.id.btnUsers);
        btnPrefs=(Button)findViewById(R.id.btnPref);
        btnAbout=(Button)findViewById(R.id.btnAbout);
        btnPrefs.setTextColor(Color.GRAY);
        LinearLayout layout=(LinearLayout)findViewById(R.id.admincon);

        txtName.setText(MainActivity.fullname);
        imgProfile=(ParseImageView)findViewById(R.id.imgProfilePhoto);
        imgProfile.setParseFile(MainActivity.imgFile);
        imgProfile.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

            }
        });
        try{
        if(MainActivity.LEVEL.equals("User")||MainActivity.LEVEL.equals("VIP")){

            layout.setVisibility(View.GONE);

        }}catch(Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }





                btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Account.class));
                finish();
            }
        });
        Button btnPending=(Button)findViewById(R.id.btnPendingFeed);
       /* Button btnEvent=(Button)findViewById(R.id.btnEvents);

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Event.class));
            }
        });*/
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PendingPost.class));

            }
        });
        btnUsers.setVisibility(View.GONE);
        btnPending.setVisibility(View.GONE);
        btnPrefs.setText("Preferences");
        if(MainActivity.LEVEL.equals("admin")){
            btnUsers.setVisibility(View.VISIBLE);
            btnPending.setVisibility(View.VISIBLE);
            btnPrefs.setText("Tools");

        }
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Preferences.this,MainActivity.class));
                finish();
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Preferences.this,ProfileViewer.class));
                finish();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Preferences.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();

                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(Preferences.this);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("status","inactive");
                editor.apply();
                finish();
                startActivity(new Intent(Preferences.this, LogIn.class));


            }
        });
        Button btnUpload=(Button)findViewById(R.id.btnChangePhoto);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),uploadlogo.class));

            }
        });
        Switch switchPush=(Switch)findViewById(R.id.switchPush);
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(Preferences.this);
        final SharedPreferences.Editor edt=pref.edit();
        String push=pref.getString("Push","Off");
        if (push.equals("On")){
            switchPush.setChecked(true);
        }else{
            switchPush.setChecked(false);
        }

        if(checkConnectivity()){
            switchPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ParsePush.subscribeInBackground("NKTI", new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                                } else {
                                    Log.e("com.parse.push", "failed to subscribe for push", e);
                                }
                                edt.putString("Push","On");
                                edt.apply();
                            }
                        });
                    }
                    else if(!isChecked){
                        ParsePush.unsubscribeInBackground("NKTI",new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(getApplicationContext(),"Action successfully executed",Toast.LENGTH_SHORT).show();
                            }
                        });
                        edt.putString("Push","Off");
                        edt.apply();
                    }
                }
            });

        }else{
            switchPush.setEnabled(false);
            layout.setVisibility(View.GONE);

        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
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
    public boolean checkConnectivity(){

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

package com.irm.nkti.nktimobilebulletin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.adapter.EventItemAdapter;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter;
import com.irm.nkti.nktimobilebulletin.com.models.EventItem;
import com.navdrawer.SimpleSideDrawer;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.erikw.PullToRefreshListView;


public class Event extends ActionBarActivity {
PullToRefreshListView lstEvent;
Button btnNewEvent;
    Dialog newEvent;
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
    Button btnPending;
    Button btnEvent;
    RelativeLayout feedContainer;
    TextView txtName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        final EventItem event=new EventItem();
        lstEvent=(PullToRefreshListView)findViewById(R.id.lstEvent);
        EventItemAdapter adapter=new EventItemAdapter(getApplicationContext());
        lstEvent.setAdapter(adapter);
        btnNewEvent=(Button)findViewById(R.id.btnNewEvent);


        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar = getSupportActionBar();
        actionBar.setTitle("COMMUNITY EVENTS");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);


        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnAccount = (Button) findViewById(R.id.btnAccount);
        txtName = (TextView) findViewById(R.id.txtName);

        btnFeed = (Button) findViewById(R.id.btnFeed);

        btnUsers = (Button) findViewById(R.id.btnUsers);
        btnPrefs = (Button) findViewById(R.id.btnPref);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnPending=(Button)findViewById(R.id.btnPendingFeed);
       // btnEvent=(Button)findViewById(R.id.btnEvents);
        txtName.setText(MainActivity.fullname);
        btnPending.setVisibility(View.GONE);
        btnEvent.setTextColor(Color.GRAY);

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkConnectivity()) {

                    newEvent = new Dialog(Event.this);
                    newEvent.setContentView(R.layout.new_post);
                    newEvent.setTitle("Create Event");
                    newEvent.show();
                    final EditText edtCOntent = (EditText) newEvent.findViewById(R.id.editText);
                    Button postContent = (Button) newEvent.findViewById(R.id.dbtnPost);
                    final Button btnRemove = (Button) newEvent.findViewById(R.id.btnRemove);
                    final Button btnAddPhoto = (Button) newEvent.findViewById(R.id.btnAddPhoto);
                    btnRemove.setVisibility(View.GONE);
                    btnAddPhoto.setVisibility(View.GONE);
                    postContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startLoading();
                            event.setContent(edtCOntent.getText().toString());
                            event.setUsername(MainActivity.fullname);
                            //event.setDepartment(MainActivity.department);
                            event.setWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                            DateFormat df = new SimpleDateFormat("EEEE, hh:mm ,MMMM dd, yyyy");
                            String mydate = df.format(Calendar.getInstance().getTime());
                            event.setTime(mydate);
                            event.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    newEvent.dismiss();
                                    stopLoading();
                                }
                            });
                        }
                    });
                }
            }
        });


     lstEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             TextView txtId=(TextView)view.findViewById(R.id.txtId);
             final String ID=txtId.getText().toString();
             final Dialog dialog=new Dialog(Event.this);
             dialog.setTitle("Event Actions");
             dialog.setContentView(R.layout.event_actions);
             dialog.show();
             Button btnJoin=(Button)dialog.findViewById(R.id.btnJoin);
             Button btnIgnore=(Button)dialog.findViewById(R.id.btnIgnore);
             btnJoin.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     startLoading();
                     ParseQuery query=ParseQuery.getQuery("Events");
                     query.getInBackground(ID,new GetCallback() {
                         @Override
                         public void done(ParseObject parseObject, ParseException e) {
                             parseObject.put("noofdelegates", parseObject.getInt("noofdelegates") + 1);
                             //String[] str=parseObject.get
                             parseObject.saveInBackground();
                             dialog.dismiss();
                             stopLoading();
                         }
                     });
                 }
             });
             btnIgnore.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dialog.dismiss();
                 }
             });

         }
     });
        lstEvent.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final FeedItemAdapter adapter;
                adapter=new FeedItemAdapter(Event.this);
                lstEvent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lstEvent.onRefreshComplete();
                        EventItemAdapter adapter=new EventItemAdapter(getApplicationContext());
                        lstEvent.setAdapter(adapter);

                    }
                },2000);
            }
        });

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PendingPost.class));

            }
        });
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Account.class));
                finish();
            }
        });
        btnUsers.setVisibility(View.GONE);
        if(MainActivity.LEVEL.equals("admin")){
            btnUsers.setVisibility(View.VISIBLE);
            btnPrefs.setText("Preferences");
        }
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this,ProfileViewer.class));
                finish();
            }
        });
        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this,Preferences.class));
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Event.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(Event.this);
                dialog.setContentView(R.layout.confirm);
                dialog.setTitle("Log Out");
                TextView phrase=(TextView)dialog.findViewById(R.id.txtText);
                phrase.setText("Do you want to log out?");
                Button yes=(Button)dialog.findViewById(R.id.btnYes);
                Button no=(Button)dialog.findViewById(R.id.btnNo);;
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logOutUser();
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();




            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
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
    public  void logOutUser(){
        ParseUser.logOut();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("status","inactive");
        editor.apply();
        finish();
        startActivity(new Intent(getApplicationContext(), LogIn.class));

    }
    private void startLoading() {
        progressDialog=new ProgressDialog(Event.this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Wait a minute......");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
    }
}

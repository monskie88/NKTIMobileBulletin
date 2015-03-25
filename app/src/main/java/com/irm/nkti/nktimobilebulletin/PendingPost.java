package com.irm.nkti.nktimobilebulletin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.adapter.PendingFeedAdapter;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.irm.nkti.nktimobilebulletin.com.models.PendingFeed;
import com.navdrawer.SimpleSideDrawer;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.erikw.PullToRefreshListView;


public class PendingPost extends ActionBarActivity {
    PullToRefreshListView lstPendingPost;
    String fullname,strContent,date,strId,week;
    ParseFile image;
    ProgressDialog progressDialog;
    PendingFeedAdapter adapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_post);
        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar = getSupportActionBar();
        actionBar.setTitle("PENDING POST");
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
        btnPending.setTextColor(Color.GRAY);


        lstPendingPost=(PullToRefreshListView)findViewById(R.id.lstPending);
        adapter=new PendingFeedAdapter(getApplicationContext());
        lstPendingPost.setAdapter(adapter);
        lstPendingPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
               final Dialog dialog=new Dialog(PendingPost.this);
                dialog.setContentView(R.layout.pending_layout);
                dialog.setTitle("Pending post actions");
                dialog.show();
                final TextView txtId=(TextView)v.findViewById(R.id.FeedId);
                Button btnApprove=(Button)dialog.findViewById(R.id.btnApproved);
                Button btnDelete=(Button)dialog.findViewById(R.id.btnDelete);
                btnApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ParseQuery query=ParseQuery.getQuery("PendingFeed");
                        startLoading();
                        query.getInBackground(txtId.getText().toString(),new GetCallback() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {

                                fullname=parseObject.getString("username");
                                date=parseObject.getString("timePosted");
                                strContent=parseObject.getString("content");
                                image=parseObject.getParseFile("feedPhoto");

                                FeedItem post=new FeedItem();
                                post.setUsername(fullname);
                                post.setContent(strContent);
                                post.setFeedPhoto(image);

                                post.setWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                                post.setTime(date);
                                post.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParsePush push = new ParsePush();
                                        push.setChannel("NKTI");
                                        push.setMessage(fullname + " posted an announcement: ");
                                        push.sendInBackground(new SendCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                stopLoading();
                                            }
                                        });
                                    }
                                });
                                parseObject.deleteInBackground();
                                PendingFeedAdapter adp=new PendingFeedAdapter(getApplicationContext());
                                lstPendingPost.setAdapter(adp);
                            }
                        });
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("PendingFeed");
                        query.getInBackground(txtId.getText().toString(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                parseObject.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Dialog dialog = new Dialog(PendingPost.this);
                                        dialog.setTitle("Deleted");
                                        dialog.setContentView(R.layout.deleted);
                                        dialog.show();
                                        PendingFeedAdapter adp=new PendingFeedAdapter(getApplicationContext());
                                        lstPendingPost.setAdapter(adp);

                                    }
                                });
                            }
                        });
                    }
                });



            }
        });
     /*   btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Event.class));
            }
        });*/

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
                startActivity(new Intent(PendingPost.this,ProfileViewer.class));
                finish();
            }
        });
        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PendingPost.this,Preferences.class));
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PendingPost.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(PendingPost.this);
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
        getMenuInflater().inflate(R.menu.menu_pending_post, menu);
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
    private void startLoading() {
        progressDialog=new ProgressDialog(PendingPost.this);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Processing post......");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
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

}

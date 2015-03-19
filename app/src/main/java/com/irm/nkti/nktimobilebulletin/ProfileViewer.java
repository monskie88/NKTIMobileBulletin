package com.irm.nkti.nktimobilebulletin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.adapter.AdminListAdapter;
import com.irm.nkti.nktimobilebulletin.com.adapter.UserFullList;
import com.irm.nkti.nktimobilebulletin.com.adapter.UserList;
import com.irm.nkti.nktimobilebulletin.com.adapter.VIPList;
import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.navdrawer.SimpleSideDrawer;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class ProfileViewer extends ActionBarActivity {
ListView lstUsers;
    SimpleSideDrawer mNav;
    ActionBar actionBar;
    ParseImageView imgProfile;
    Button btnLogOut;
    Button btnAccount;
    Button btnFeed;
    Button btnUsers;
    Button btnPrefs;
    Button btnAbout;
    RadioButton rdAdmin,rdVIP,rdUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_viewer);
        lstUsers    =(ListView)findViewById(R.id.listUser);

        rdAdmin=(RadioButton)findViewById(R.id.rdAdmin);
        rdVIP=(RadioButton)findViewById(R.id.rdVIP);
        rdUser=(RadioButton)findViewById(R.id.rdUser);

        rdAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AdminListAdapter adapter=new AdminListAdapter(ProfileViewer.this);
                    lstUsers.setAdapter(adapter);
                }

            }
        });
        rdVIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    VIPList vip=new VIPList(getApplicationContext());
                    lstUsers.setAdapter(vip);

                }
            }
        });
        rdUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserList user=new UserList(getApplicationContext());
                    lstUsers.setAdapter(user);

                }
            }
        });




        final UserFullList adapter=new UserFullList(ProfileViewer.this);

        lstUsers.setAdapter(adapter);
        lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(checkConnectivity()){
                    final TextView txtView = (TextView) view.findViewById(R.id.txtFname);
                    final TextView txtLevel=(TextView)view.findViewById(R.id.txtLevel);
                    final TextView txtId=(TextView)view.findViewById(R.id.txtCode);
                    final Dialog dialog=new Dialog(ProfileViewer.this);
                    dialog.setContentView(R.layout.promotion);
                    dialog.setTitle("Action");
                    dialog.show();
                    AdminList mod =new AdminList();
                    final String level=txtLevel.getText().toString();
                    Button btnPromote=(Button)dialog.findViewById(R.id.btnPromote);
                    Button btnDemote=(Button)dialog.findViewById(R.id.btnDemote);
                    btnPromote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(level.equals("admin")){
                                dialog.dismiss();
                                //Toast.makeText(ProfileViewer.this,"Highest privilege granted!",Toast.LENGTH_SHORT).show();





                            }
                            else if(level.equals("VIP")){
                                Toast.makeText(getApplicationContext(),"Admin privilege granted!",Toast.LENGTH_SHORT).show();
                                ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
                                query.whereEqualTo("fullname",txtView.getText().toString());
                                query.getFirstInBackground(new GetCallback<AdminList>() {
                                    @Override
                                    public void done(AdminList adminList, ParseException e) {
                                        adminList.setLevel("admin");
                                        adminList.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                            }
                                        });
                                    }
                                });

                                lstUsers.setAdapter(adapter);
                            }
                            else if(level.equals("User")){
                                //Toast.makeText(getApplicationContext(),"VIP privilege granted!",Toast.LENGTH_SHORT).show();
                                ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
                                query.getInBackground(txtId.getText().toString(),new GetCallback<AdminList>() {
                                    @Override
                                    public void done(AdminList adminList, ParseException e) {
                                        adminList.setLevel("VIP");
                                        adminList.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                //Toast.makeText(ProfileViewer.this,e.toString(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                });
                                lstUsers.setAdapter(adapter);
                            }

                        }
                    });
                    btnDemote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(level.equals("admin")){
                                dialog.dismiss();
                                //Toast.makeText(ProfileViewer.this,"Highest privilege granted!",Toast.LENGTH_SHORT).show();
                                ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
                                query.whereEqualTo("fullname",txtView.getText().toString());
                                query.getFirstInBackground(new GetCallback<AdminList>() {
                                    @Override
                                    public void done(AdminList adminList, ParseException e) {
                                        adminList.setLevel("VIP");
                                        adminList.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                            }
                                        });
                                    }
                                });

                                lstUsers.setAdapter(adapter);




                            }
                            else if(level.equals("VIP")){
                                //Toast.makeText(getApplicationContext(),"Admin privilege granted!",Toast.LENGTH_SHORT).show();
                                ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
                                query.whereEqualTo("fullname",txtView.getText().toString());
                                query.getFirstInBackground(new GetCallback<AdminList>() {
                                    @Override
                                    public void done(AdminList adminList, ParseException e) {
                                        adminList.setLevel("User");
                                        adminList.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                            }
                                        });
                                    }
                                });

                                lstUsers.setAdapter(adapter);
                            }
                            else if(level.equals("User")){
                                //Toast.makeText(getApplicationContext(),"VIP privilege granted!",Toast.LENGTH_SHORT).show();

                                lstUsers.setAdapter(adapter);
                            }



                        }
                    });

                }else{
                    Dialog dialog=new Dialog(ProfileViewer.this);
                    dialog.setTitle("Connectivity Error");
                    dialog.setContentView(R.layout.nonetwork);
                    dialog.show();
                }

            }
        });



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
                startActivity(new Intent(ProfileViewer.this, MainActivity.class));
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
                startActivity(new Intent(ProfileViewer.this,Preferences.class));
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileViewer.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();

                SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(ProfileViewer.this);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("status","inactive");
                editor.apply();
                finish();
                startActivity(new Intent(ProfileViewer.this, LogIn.class));


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_viewer, menu);
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

package com.irm.nkti.nktimobilebulletin;

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.navdrawer.SimpleSideDrawer;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class Account extends ActionBarActivity {
    SimpleSideDrawer mNav;
    ActionBar actionBar;
    EditText edtFullname,edtPosition,edtUsername,edtPassword;
    ParseImageView imgProfile;
    CheckBox chkEdit;
    Button btnFeeds;
    Button btnLogOut;
    Button btnAccount;
    Button btnFeed;
    Button btnUsers;
    Button btnPrefs;
    Button btnAbout;
    Button btnChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mNav=new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        edtFullname=(EditText)findViewById(R.id.edtUpdateFullname);
        edtPosition=(EditText)findViewById(R.id.edtUpdatePosition);
        edtUsername=(EditText)findViewById(R.id.edtUpdateUsername);
        //edtPassword=(EditText)findViewById(R.id.edtUpdatePassword);
        btnLogOut=(Button)findViewById(R.id.btnLogOut);
        btnAccount=(Button)findViewById(R.id.btnAccount);
        TextView txtName=(TextView)findViewById(R.id.txtName);
        btnFeed=(Button)findViewById(R.id.btnFeed);
        btnAccount.setTextColor(Color.GRAY);
        btnUsers=(Button)findViewById(R.id.btnUsers);
        btnPrefs=(Button)findViewById(R.id.btnPref);
        btnAbout=(Button)findViewById(R.id.btnAbout);
        final CheckBox chkEdit=(CheckBox)findViewById(R.id.chkEdit);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this,MainActivity.class));
            }
        });


        List<String> department=new ArrayList<>();

        department.add("IRM");
        department.add("OPD");
        department.add("GSD");
        department.add("HRD");
        final Spinner spnDepartment=(Spinner)findViewById(R.id.UpdatespnDepartment);

        final ArrayAdapter<String> adpDept= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,department);
        adpDept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepartment.setAdapter(adpDept);

        final EditText edtFullname=(EditText)findViewById(R.id.edtUpdateFullname);
        final EditText edtPosition=(EditText)findViewById(R.id.edtUpdatePosition);
        final EditText edtUsername=(EditText)findViewById(R.id.edtUpdateUsername);
       // EditText edtFullname=(EditText)findViewById(R.id.edtUpdateFullname);
        final TextView txtID=(TextView)findViewById(R.id.txtID);
        if(checkConnectivity()){
            ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
            query.whereEqualTo("fullname",MainActivity.fullname);
            query.getFirstInBackground(new GetCallback<AdminList>() {
                @Override
                public void done(AdminList adminList, ParseException e) {
                    edtFullname.setText(adminList.getfullname());
                    edtPosition.setText(adminList.getPosition());
                    edtUsername.setText(ParseUser.getCurrentUser().getUsername());
                    spnDepartment.setSelection(adpDept.getPosition(adminList.getDepartment()));
                    txtID.setText(adminList.getId());
                }
            });
        }else{
            chkEdit.setEnabled(false);
        }

        edtFullname.setEnabled(false);
        edtPosition.setEnabled(false);
        edtUsername.setEnabled(false);
        spnDepartment.setEnabled(false);

        chkEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtFullname.setEnabled(true);
                    edtPosition.setEnabled(true);
                   // edtUsername.setEnabled(true);
                    spnDepartment.setEnabled(true);
                }
                else {
                    edtFullname.setEnabled(false);
                    edtPosition.setEnabled(false);
                    edtUsername.setEnabled(false);
                    spnDepartment.setEnabled(false);
                }
            }
        });
        Button btnUpdateAccount=(Button)findViewById(R.id.btnUpdate);
        btnUpdateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<AdminList> query=ParseQuery.getQuery(AdminList.class);
                query.getInBackground(txtID.getText().toString(),new GetCallback<AdminList>() {
                    @Override
                    public void done(AdminList adminList, ParseException e) {
                        adminList.put("fullname",edtFullname.getText().toString());
                        //adminList.put("username", edtUsername.getText().toString());
                        adminList.put("position",edtPosition.getText().toString());
                        adminList.put("department", spnDepartment.getSelectedItem().toString());
                        adminList.saveInBackground();
                        ParseUser.logOut();
                        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(Account.this);
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.putString("status","inactive");
                        editor.apply();
                        finish();

                    }
                });
               /* ParseQuery<ParseUser> user=ParseUser.getQuery();
                user.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(final ParseUser parseUser, ParseException e) {
                        parseUser.setUsername(edtUsername.getText().toString());
                        parseUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                chkEdit.setChecked(false);
                                ParseUser.logOut();
                            }
                        });
                    }
                });*/

            }

        });
        btnChangePass=(Button)findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(Account.this);
                dialog.setTitle("Change Password");
                dialog.setContentView(R.layout.newpassword);
                dialog.show();

                Button btnUpdatePass=(Button)dialog.findViewById(R.id.btnChangePass);
                final EditText edtCurrentPass=(EditText)dialog.findViewById(R.id.edtcurrentpass);
                final EditText edtNewPass=(EditText)dialog.findViewById(R.id.edtnewpass);
                final EditText edtNewPasscheck=(EditText)dialog.findViewById(R.id.edtnewpass2);
                final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(Account.this);
                final String pass=prefs.getString("currentpass","null");
                final String username=ParseUser.getCurrentUser().getUsername();

                btnChangePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtCurrentPass.getText().toString().equals(prefs.getString("currentpass","null"))){
                            if (edtNewPass.getText().toString().equals("")||(edtNewPass.getText().toString().equals(""))){
                                if(edtNewPass.getText().toString().equals(edtNewPasscheck.getText().toString())){
                                    ParseUser user=ParseUser.getCurrentUser();
                                    user.setPassword(edtNewPass.getText().toString());
                                    user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                        }
                                    });


                                }else{
                                    Toast.makeText(getApplicationContext(),"Password doesnt match.",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Empty values not allowed!.",Toast.LENGTH_SHORT).show();
                                edtNewPass.setText("");
                                edtNewPasscheck.setText("");
                            }


                        }else{
                            Toast.makeText(getApplicationContext(),"Access Denied",Toast.LENGTH_SHORT).show();
                        }
                    }
                });





            }
        });
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),About.class));
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
        getMenuInflater().inflate(R.menu.menu_account, menu);
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

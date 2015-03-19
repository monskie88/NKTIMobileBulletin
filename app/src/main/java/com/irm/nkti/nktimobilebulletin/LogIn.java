package com.irm.nkti.nktimobilebulletin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;


public class LogIn extends Activity {
    Button btnSignUp,btnLogin;
    EditText edtUsername,edtPassword;
    static List<String> departmentList;
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    //SpinnerAdapter adapter=new SpinnerAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Parse.initialize(this, "VRsvlABLhL1pm8zqcbuKS3RZxOTMfStrbkvVd6An", "1QONJCeJEKa9nFqyh3kn9z2m1EjfRlI1DZG4RIyr");
        LogStatus();
        //departmentList=adapter.getDepartment(getApplicationContext());
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnLogin=(Button)findViewById(R.id.btnLogIn);
        edtUsername=(EditText)findViewById(R.id.edtUsername);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        progressDialog=new ProgressDialog(LogIn.this);
        //checkSession();
        progressDialog.setTitle("NKTI");
        progressDialog.setMessage("Logging In");
        progressDialog.setCancelable(false);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnectivity()){
                    Intent i=new Intent(LogIn.this,SignUp.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Dialog dialog=new Dialog(LogIn.this);
                    dialog.setTitle("Connectivity Error");
                    dialog.setContentView(R.layout.nonetwork);
                    dialog.show();

                }



            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
              @Override
            public void onClick(View v) {
                  if (checkConnectivity()) {
                      if (edtUsername.getText().toString().equals("") || edtPassword.getText().toString().equals("")) {
                          Toast.makeText(getApplicationContext(), "Cannot Log in with null values!", Toast.LENGTH_SHORT).show();
                      } else {
                          startLoading();
                          try {
                              ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                                  public void done(ParseUser user, ParseException e) {

                                      if (user != null) {
                                          // Hooray! The user is logged in.

                                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                          editor.putString("status", "active");
                                          editor.putString("currentpass",edtPassword.getText().toString());
                                          editor.apply();
                                          finish();

                                      } else {
                                          // Signup failed. Look at the ParseException to see what happened.
                                          Toast.makeText(LogIn.this,"Access Denied! Wrong username or password.",Toast.LENGTH_LONG).show();
                                          finish();
                                          startActivity(new Intent(getApplicationContext(),LogIn.class));

                                      }stopLoading();
                                  }
                              });
                          }catch (Exception e){
                              Toast.makeText(LogIn.this,"Access Denied! Wrong username or password.",Toast.LENGTH_LONG).show();

                          }


                      }
                  }else{
                      Dialog dialog=new Dialog(LogIn.this);
                      dialog.setTitle("Connectivity Error");
                      dialog.setContentView(R.layout.nonetwork);
                      dialog.show();
                  }
              }


            });


    }

    private void LogStatus() {
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor=prefs.edit();
        String status=prefs.getString("status","inactive");
            if(status.equals("active")){
                startActivity(new Intent(this,MainActivity.class));
                finish();

            }
        else if(status.equals("inactive")){
                editor.putString("status", "inactive");
                editor.apply();
            }

    }

    private void startLoading() {
        try{

        progressDialog.show();
        }catch (Exception e){


        }

    }
    private void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
    }

    private void checkSession() {
    try{
        if(ParseUser.getCurrentUser().getSessionToken()!=(null)){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        }
    }catch (NullPointerException e){

    }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

package com.irm.nkti.nktimobilebulletin;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.irm.nkti.nktimobilebulletin.com.adapter.AdminListAdapter;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter_Month;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter_Today;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter_Week;
import com.irm.nkti.nktimobilebulletin.com.adapter.FeedItemAdapter_Year;
import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.irm.nkti.nktimobilebulletin.com.models.PendingFeed;
import com.navdrawer.SimpleSideDrawer;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.erikw.PullToRefreshListView;


public class MainActivity extends ActionBarActivity {
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
    RelativeLayout feedContainer;
    TextView txtName;
    Bitmap img;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    public static String objectId, username,fullname;
    PullToRefreshListView lstFeeds;
    public static ParseFile imgFile;
    ProgressDialog progressDialog;
    ParseUser currentUser;
    Dialog newPost;
    String LEVEL_OF_ACCESSIBILITY;
    static String LEVEL;
    ParseFile photo;
    SpinnerAdapter spnAdapter;
    int currentAdapter=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseObject.registerSubclass(FeedItem.class);
        //Parse.enableLocalDatastore(MainActivity.this);
        String[] items = {"Today", "Yesterday", "Week", "Last Week", "Month", "Last Month"};
        Toast.makeText(getApplicationContext(), ("" + Calendar.getInstance().get(Calendar.DATE)), Toast.LENGTH_LONG).show();


        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.left_drawer);
        actionBar = getSupportActionBar();
        actionBar.setTitle("COMMUNITY FEED");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);


        final ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int i, long l) {
                String str = spnAdapter.getItem(i).toString();
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                return false;
            }
        };



        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnAccount = (Button) findViewById(R.id.btnAccount);
        txtName = (TextView) findViewById(R.id.txtName);
        lstFeeds = (PullToRefreshListView) findViewById(R.id.lstFeed);
        btnFeed = (Button) findViewById(R.id.btnFeed);
        btnFeed.setTextColor(Color.GRAY);
        btnUsers = (Button) findViewById(R.id.btnUsers);
        btnPrefs = (Button) findViewById(R.id.btnPref);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnPending=(Button)findViewById(R.id.btnPendingFeed);


        progressDialog = new ProgressDialog(MainActivity.this);

        feedContainer = (RelativeLayout) findViewById(R.id.feedcontainer);
        btnUsers.setVisibility(View.GONE);
        btnNewPost = (Button) findViewById(R.id.btnNewPost);
        getKeyInfo();

        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnectivity()) {

                newPost = new Dialog(MainActivity.this);
                newPost.setContentView(R.layout.new_post);
                newPost.setTitle("Create Post");
                newPost.show();
                final EditText edtCOntent = (EditText) newPost.findViewById(R.id.editText);
                Button postContent = (Button) newPost.findViewById(R.id.dbtnPost);
                final Button btnRemove = (Button) newPost.findViewById(R.id.btnRemove);
                final Button btnAddPhoto = (Button) newPost.findViewById(R.id.btnAddPhoto);
                btnRemove.setVisibility(View.GONE);

                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                btnAddPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 0);
                        btnAddPhoto.setText("Change Photo");
                        btnRemove.setVisibility(View.VISIBLE);


                    }
                });
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img = null;
                        btnRemove.setVisibility(View.GONE);
                    }
                });
                postContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtCOntent.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Cannot post with empty content", Toast.LENGTH_LONG).show();
                        } else {
                            startLoading();
                            newPost.dismiss();
                            PendingFeed post = new PendingFeed();
                            post.setUsername(fullname);
                            post.setContent(edtCOntent.getText().toString());

                            post.setWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                            DateFormat df = new SimpleDateFormat("EEEE, hh:mm ,MMMM dd, yyyy");
                            String mydate = df.format(Calendar.getInstance().getTime());
                            post.setTime(mydate);
                            try {
                                post.setFeedPhoto(saveAsParseFile());
                                post.setAttachment(1);
                                img = null;
                            } catch (Exception e) {
                                Drawable myDrawable = getResources().getDrawable(R.drawable.def);
                                Bitmap draw = ((BitmapDrawable) myDrawable).getBitmap();
                                img = draw;
                                post.setFeedPhoto(saveAsParseFile());
                                img = null;
                                post.setAttachment(0);

                            }


                            //post.setAuthor(currentUser.getObjectId());
                            post.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    stopLoading();
                                   /* if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Successfully posted", Toast.LENGTH_SHORT).show();
                                        ParsePush push = new ParsePush();
                                        push.setChannel("NKTI");
                                        push.setMessage(fullname + " posted an announcement: " + edtCOntent.getText().toString());
                                        push.sendInBackground();
                                        stopLoading();


                                        new retrieveFeeds().execute();


                                    } else {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                    }*/

                                }
                            });
                        }


                    }
                });
                Button btnCancel = (Button) newPost.findViewById(R.id.dbtnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPost.dismiss();
                    }

                });
            }else{
                    Dialog dialog=new Dialog(MainActivity.this);
                    dialog.setTitle("Connectivity Error");
                    dialog.setContentView(R.layout.nonetwork);
                    dialog.show();
                }
            }


        });
        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PendingPost.class));

            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Account.class));
                finish();
            }
        });
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileViewer.class));
                finish();
            }
        });
        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Preferences.class));
                finish();
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,About.class));
                finish();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(MainActivity.this);
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

        imgProfile=(ParseImageView)findViewById(R.id.imgProfilePhoto);
         //setProfileImage();

      /* imgProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final Dialog newDialog=new Dialog(MainActivity.this);
               newDialog.setContentView(R.layout.chooseaction_profilepicture);
               newDialog.setTitle("Choose Action");
               newDialog.show();
               Button changeProfileP=(Button)newDialog.findViewById(R.id.btnChangePphoto);
               Button btnViewPhoto=(Button)newDialog.findViewById(R.id.btnViewPhoto);
               Button close=(Button)newDialog.findViewById(R.id.btnClosePhoto);
               changeProfileP.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent();
                       intent.setType("image*//*");
                       intent.setAction(Intent.ACTION_GET_CONTENT);
                       startActivityForResult(Intent.createChooser(intent,
                               "Select Picture"), SELECT_PICTURE);
                       newDialog.dismiss();
                   }
               });


           }
       });*/

        lstFeeds.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final FeedItemAdapter adapter;
                adapter=new FeedItemAdapter(MainActivity.this);
                lstFeeds.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lstFeeds.onRefreshComplete();
                        int adp=getCurrentAdapter();
                        setAdapter(adp);
                    }
                },2000);
            }
        });
    new retrieveFeeds().execute();

     lstFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(LEVEL_OF_ACCESSIBILITY.equals("admin")){
                final TextView txtId = (TextView) view.findViewById(R.id.FeedId);
                if (txtName.getText().toString().equals(fullname)) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.feed_action);
                    dialog.setTitle("Feed Action");
                    dialog.show();
                    Button btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
                    Button btnDel = (Button) dialog.findViewById(R.id.btnDelete);
                    btnDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Delete the post
                            dialog.dismiss();
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("FeedItems");
                            query.getInBackground(txtId.getText().toString(), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    parseObject.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Dialog dialog = new Dialog(MainActivity.this);
                                            dialog.setTitle("Deleted");
                                            dialog.setContentView(R.layout.deleted);
                                            dialog.show();
                                            setAdapter(getCurrentAdapter());
                                        }
                                    });
                                }
                            });
                        }
                    });
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "UNDER CONSTRUCTION", Toast.LENGTH_LONG).show();
                            //Edit post

                        }
                    });
                }


            }
            }
        });


 }


    private void getKeyInfo() {

        currentUser=ParseUser.getCurrentUser();
        if(currentUser!=null){

            ParseQuery<ParseObject> query=ParseQuery.getQuery("UserDetails");
            query.whereEqualTo("username",currentUser.getUsername());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    txtName.setText(parseObject.getString("fullname"));
                    fullname=txtName.getText().toString();
                    LEVEL_OF_ACCESSIBILITY=parseObject.getString("userType");
                    LEVEL=LEVEL_OF_ACCESSIBILITY;
                    if(LEVEL_OF_ACCESSIBILITY.equals("User")){
                        btnNewPost.setVisibility(View.GONE);
                    }
                    else{
                        btnNewPost.setVisibility(View.VISIBLE);
                    }
                    if(LEVEL_OF_ACCESSIBILITY.equals("admin")){
                        btnUsers.setVisibility(View.VISIBLE);
                        btnPrefs.setText("Preferences");
                    }

                }
            });

    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                img=BitmapFactory.decodeFile(selectedImagePath);
                //imgProfile.setImageURI(selectedImageUri);
                Bitmap scaledBitmap=Bitmap.createScaledBitmap(img,img.getWidth(),img.getHeight(),false);

                img=scaledBitmap;

                //getKeyInfo();
                //setProfileImage();

            }
        }
    }

    private String getPath(Uri uri) {
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    private ParseFile saveAsParseFile() {
        //convert image to bitmap


            //startLoading();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 35, stream);
            byte[] data = stream.toByteArray();
            final ParseFile imgFile = new ParseFile(currentUser.getObjectId(), data);
            //convert image to bitmap
            return imgFile;




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
        //    return true;
       // }
        switch(item.getItemId()){
            case android.R.id.home:
                mNav.toggleLeftDrawer();
                break;
            case R.id.feedretrieve:
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.feed_to_retrieve);
                dialog.setTitle("Feeds to Retrieve");
                dialog.show();
                Button btnToday=(Button)dialog.findViewById(R.id.btnToday);
                Button btnWeek=(Button)dialog.findViewById(R.id.btnWeek);
                Button btnMonth=(Button)dialog.findViewById(R.id.btnMonth);
                Button btnYear=(Button)dialog.findViewById(R.id.btnYear);
                if(getCurrentAdapter()==1){
                    btnToday.setEnabled(false);

                }
                else if(getCurrentAdapter()==2){
                    btnWeek.setEnabled(false);
                }
                else if(getCurrentAdapter()==3){
                    btnMonth.setEnabled(false);
                }
                else if(getCurrentAdapter()==4){
                    btnYear.setEnabled(false);
                }
                btnToday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedItemAdapter_Today adapter=new FeedItemAdapter_Today(MainActivity.this);
                        lstFeeds.setAdapter(adapter);
                        setCurrentAdapter(1);
                        dialog.dismiss();
                    }
                });
                btnWeek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedItemAdapter_Week adapter= new FeedItemAdapter_Week(MainActivity.this);
                        lstFeeds.setAdapter(adapter);
                        setCurrentAdapter(2);
                        dialog.dismiss();

                    }
                });
                btnMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedItemAdapter_Month adapter= new FeedItemAdapter_Month(MainActivity.this);
                        lstFeeds.setAdapter(adapter);
                        setCurrentAdapter(3);
                        dialog.dismiss();

                    }
                });
                btnYear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedItemAdapter_Year adapter= new FeedItemAdapter_Year(MainActivity.this);
                        lstFeeds.setAdapter(adapter);
                        setCurrentAdapter(4);
                        dialog.dismiss();

                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }



    class retrieveFeeds extends AsyncTask<Void,Void,String>{
        FeedItemAdapter adapter;
        AdminListAdapter adapter1;
        @Override
        protected String doInBackground(Void... params) {


            adapter=new FeedItemAdapter(MainActivity.this);
            adapter1=new AdminListAdapter(MainActivity.this);


            return "Success";
        }
        ProgressDialog pDialog=new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setTitle("NKTI");
            pDialog.setMessage("Retrieving feeds");
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                pDialog.dismiss();
                pDialog=null;


            try{
                //adapter.loadObjects();
                int adp=getCurrentAdapter();
                setAdapter(adp);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void startLoading() {
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Uploading your post.Uploading speed depends on your connectivity.");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void stopLoading(){
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new retrieveFeeds().execute();
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
    public void setCurrentAdapter(int adapter){
        currentAdapter=adapter;
    }
    public int getCurrentAdapter(){
    return currentAdapter;
    }
    public void setAdapter(int i){
        switch(i){
            case 1:
                FeedItemAdapter_Today adapter=new FeedItemAdapter_Today(MainActivity.this);
                lstFeeds.setAdapter(adapter);
                break;

            case 2:
                FeedItemAdapter_Week adapter_week=new FeedItemAdapter_Week(MainActivity.this);
                lstFeeds.setAdapter(adapter_week);
                break;

            case 3:
                FeedItemAdapter_Month adapter_month=new FeedItemAdapter_Month(MainActivity.this);
                lstFeeds.setAdapter(adapter_month);
                break;
            case 4:
                FeedItemAdapter_Year adapter_year=new FeedItemAdapter_Year(MainActivity.this);
                lstFeeds.setAdapter(adapter_year);
                break;


        }
    }
}


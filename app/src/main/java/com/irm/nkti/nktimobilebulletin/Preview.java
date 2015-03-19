package com.irm.nkti.nktimobilebulletin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class Preview extends ActionBarActivity {
  byte[] bytefile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        final TextView txtUsername=(TextView)findViewById(R.id.txtUsername);
        final TextView txtTime=(TextView)findViewById(R.id.txtTime);
        final TextView txtContent=(TextView)findViewById(R.id.txtContent);
        ParseImageView feedPhoto = (ParseImageView)findViewById(R.id.feedPhoto);


        ParseQuery<FeedItem> feedItem=ParseQuery.getQuery(FeedItem.class);
       feedItem.getInBackground(getIntent().getStringExtra("objectId"),new GetCallback<FeedItem>() {
            @Override
            public void done(FeedItem object, ParseException e) {
                txtContent.setText(object.getStrPost());
                txtTime.setText(object.getTimePosted());
                txtUsername.setText(object.getUsername());
                ParseFile file=(ParseFile)object.get("Photo");
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        bytefile=bytes;
                    }
                });


            }


        });
        Bitmap bitmap=BitmapFactory.decodeByteArray(bytefile,0,bytefile.length);
        feedPhoto.setImageBitmap(bitmap);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
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
}

package com.irm.nkti.nktimobilebulletin;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.com.adapter.PendingFeedAdapter;
import com.irm.nkti.nktimobilebulletin.com.models.PendingFeed;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import eu.erikw.PullToRefreshListView;


public class PendingPost extends ActionBarActivity {
    PullToRefreshListView lstPendingPost;
    String fullname,strContent,date,strId,week;
    ParseFile image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_post);
        lstPendingPost=(PullToRefreshListView)findViewById(R.id.lstPending);
        PendingFeedAdapter adapter=new PendingFeedAdapter(getApplicationContext());
        lstPendingPost.setAdapter(adapter);
        lstPendingPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView txtId=(TextView)v.findViewById(R.id.FeedId);
                ParseQuery query=ParseQuery.getQuery("PendingFeed");
                query.getInBackground(txtId.getText().toString(),new GetCallback() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        fullname=parseObject.getString("username");
                        date=parseObject.getString("timePosted");
                        strContent=parseObject.getString("content");
                        image=parseObject.getParseFile("feedPhoto");
                        //post.setAuthor(currentUser.getObjectId());

                    }
                });
                ParsePush push = new ParsePush();
                push.setChannel("NKTI");
                push.setMessage(fullname + " posted an announcement: ");
                push.sendInBackground();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

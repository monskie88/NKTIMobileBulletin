package com.irm.nkti.nktimobilebulletin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.irm.nkti.nktimobilebulletin.com.adapter.PendingFeedAdapter;

import eu.erikw.PullToRefreshListView;


public class PendingPost extends ActionBarActivity {
    PullToRefreshListView lstPendingPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_post);
        lstPendingPost=(PullToRefreshListView)findViewById(R.id.lstPending);
        PendingFeedAdapter adapter=new PendingFeedAdapter(getApplicationContext());
        lstPendingPost.setAdapter(adapter);
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

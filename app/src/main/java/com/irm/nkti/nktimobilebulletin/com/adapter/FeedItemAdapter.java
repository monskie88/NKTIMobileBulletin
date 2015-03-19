package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import bolts.Task;

/**
 * Created by MONSKIE on 2/3/2015.
 */
public class FeedItemAdapter extends ParseQueryAdapter<FeedItem>{
    ParseFile file;
    public FeedItemAdapter(Context context){
        super(context,new ParseQueryAdapter.QueryFactory<FeedItem>(){
            public ParseQuery<FeedItem> create(){

                ParseQuery query =ParseQuery.getQuery("FeedItems");
                //uery.whereEqualTo("timePosted",);
                query.orderByDescending("createdAt");

                //query.findInBackground();

                return query;

            }
        });


    }
    String time;
    private void setTime(String str) {
       time=str;
    }
    private String getTime(){
        return time;
    }


    @Override
    public View getItemView(FeedItem object, View v, ViewGroup parent) {


        if (v==null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.feed_row,parent,false);
        }
        super.getItemView(object, v, parent);



        TextView txtUsername=(TextView)v.findViewById(R.id.txtUsername);
        txtUsername.setText(object.getUsername());

        final TextView txtTime=(TextView)v.findViewById(R.id.txtTime);
        txtTime.setText(object.getTimePosted());

        TextView txtContent=(TextView)v.findViewById(R.id.txtContent);
        txtContent.setText(object.getStrPost());

        TextView txtId=(TextView)v.findViewById(R.id.FeedId);
        txtId.setText(object.getId());

       // TextView txtTap=(TextView)v.findViewById(R.id.imageNotify);

        ParseImageView img=(ParseImageView)v.findViewById(R.id.imgPhoto);
        img.setParseFile(object.getUserPhoto());


           // txtTap.setVisibility(View.GONE);


        final ParseImageView feedPhoto = (ParseImageView) v.findViewById(R.id.feedPhoto);
        feedPhoto.setParseFile(object.getFeedPhoto());
        feedPhoto.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

            }
        });




        img.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

            }
        });


        return v;

    }
}

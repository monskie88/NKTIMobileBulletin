package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MONSKIE on 3/11/2015.
 */
public class FeedItemAdapter_Today extends ParseQueryAdapter<FeedItem> {
    public FeedItemAdapter_Today(Context context){
        super(context, new QueryFactory<FeedItem>() {
            @Override
            public ParseQuery<FeedItem> create() {
                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                String mydate = df.format(Calendar.getInstance().getTime());
                ParseQuery query=ParseQuery.getQuery("FeedItems");

                query.whereEndsWith("timePosted",(""+ mydate));
                query.orderByDescending("createdAt");
                //query.orderByAscending("createdAt");
                return query;
            }
        });
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


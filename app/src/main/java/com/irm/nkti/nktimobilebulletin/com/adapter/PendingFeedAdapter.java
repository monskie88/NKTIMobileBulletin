package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.irm.nkti.nktimobilebulletin.com.models.PendingFeed;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by MONSKIE on 3/19/2015.
 */
public class PendingFeedAdapter extends ParseQueryAdapter<PendingFeed> {
    public PendingFeedAdapter(Context context){
        super(context,new QueryFactory<PendingFeed>() {
            @Override
            public ParseQuery<PendingFeed> create() {
                ParseQuery query=ParseQuery.getQuery("PendingFeed");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    @Override
    public View getItemView(PendingFeed object, View v, ViewGroup parent) {
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

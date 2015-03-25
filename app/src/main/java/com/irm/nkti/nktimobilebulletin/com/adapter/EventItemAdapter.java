package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.com.models.EventItem;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by MONSKIE on 3/24/2015.
 */
public class EventItemAdapter extends ParseQueryAdapter<EventItem> {
    public EventItemAdapter(Context context){
        super(context,new QueryFactory<EventItem>() {
            @Override
            public ParseQuery<EventItem> create() {
                ParseQuery query=ParseQuery.getQuery("Events");
                query.orderByDescending("createdAt");
                return query;
            }
        });
    }

    @Override
    public View getItemView(EventItem object, View v, ViewGroup parent) {
        if (v==null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.event_row,parent,false);
        }
        super.getItemView(object, v, parent);
        TextView txtName=(TextView)v.findViewById(R.id.txtFullname);
       //TextView txtDepartment=(TextView)v.findViewById(R.id.txtDepartment);
        TextView txtTime=(TextView)v.findViewById(R.id.txtTime);
        TextView txtContent=(TextView)v.findViewById(R.id.txtContent);
        TextView txtCount=(TextView)v.findViewById(R.id.txtCount);
        TextView txtId=(TextView)v.findViewById(R.id.txtId);
        txtId.setText(object.getId());
        txtName.setText(object.getUsername());
        txtTime.setText(object.getTimePosted());
        txtContent.setText(object.getContent());
        txtCount.setText(""+object.getDelegates());










        return v;
    }
}

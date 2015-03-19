package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by MONSKIE on 3/12/2015.
 */
public class VIPList extends ParseQueryAdapter<AdminList> {
    public VIPList(Context context){
        super(context,new QueryFactory<AdminList>() {
            @Override
            public ParseQuery<AdminList> create() {
                ParseQuery query=ParseQuery.getQuery("UserDetails");
                query.whereEqualTo("userType","VIP");
                return query;
            }
        });
    }

    @Override
    public View getItemView(AdminList object, View v, ViewGroup parent) {
        if (v==null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.userslist,parent,false);
        }
        TextView txtName=(TextView)v.findViewById(R.id.txtFname);
        txtName.setText(object.getfullname());
        txtName.setTextColor(Color.BLACK);
        TextView txtPosition=(TextView)v.findViewById(R.id.txtPosition_user);
        TextView txtDept=(TextView)v.findViewById(R.id.txtDepartmentuser);
        TextView txtLevel=(TextView)v.findViewById(R.id.txtLevel);
        txtPosition.setText(object.getPosition());
        txtDept.setText(object.getDepartment());
        txtLevel.setText(object.getLevel());
        TextView txtId=(TextView)v.findViewById(R.id.txtCode);
        txtId.setText(object.getId());
        return v;

    }
}

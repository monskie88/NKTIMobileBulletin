package com.irm.nkti.nktimobilebulletin.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irm.nkti.nktimobilebulletin.R;
import com.irm.nkti.nktimobilebulletin.User;
import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

/**
 * Created by MONSKIE on 2/24/2015.
 */
public class AdminListAdapter extends ParseQueryAdapter<AdminList> {
    public AdminListAdapter(Context context){
    super(context, new ParseQueryAdapter.QueryFactory<AdminList>() {
        @Override
        public ParseQuery<AdminList> create() {
            ParseQuery query =ParseQuery.getQuery("UserDetails");
            query.whereEqualTo("userType","admin");
            //query.orderByAscending("fullname");
            //query.findInBackground();
            return query;
        }
    });}

    @Override
    public View getItemView(AdminList object, View v, ViewGroup parent) {

        if (v==null){
            LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.userslist,parent,false);
        }
        TextView txtName=(TextView)v.findViewById(R.id.txtFname);
        txtName.setText(object.getfullname());
        TextView txtPosition=(TextView)v.findViewById(R.id.txtPosition_user);
        TextView txtDept=(TextView)v.findViewById(R.id.txtDepartmentuser);
        TextView txtLevel=(TextView)v.findViewById(R.id.txtLevel);
        txtPosition.setText(object.getPosition());
        txtDept.setText(object.getDepartment());
        txtLevel.setText(object.getLevel());
        TextView txtId=(TextView)v.findViewById(R.id.txtCode);
        txtId.setText(object.getId());
        TextView txtPromotion=(TextView)v.findViewById(R.id.txtpromotedby);
        txtPromotion.setText(object.getPromotion());
        TextView txtDemotion=(TextView)v.findViewById(R.id.txtdemotedby);
        txtDemotion.setText(object.getDemotion());
        TextView txtPromotionDate=(TextView)v.findViewById(R.id.txtPromotionEffectivity);
        txtPromotionDate.setText(object.getPromotionDate());
        TextView txtDemotionDate=(TextView)v.findViewById(R.id.txtDemotionEffectivity);
        txtDemotionDate.setText(object.getDemotionDate());
        return v;
    }
}


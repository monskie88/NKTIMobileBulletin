package com.irm.nkti.nktimobilebulletin.com.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by MONSKIE on 2/24/2015.
 */
@ParseClassName("UserDetails")
public class AdminList extends ParseObject {

    public AdminList(){


    }
    public String getfullname(){return getString("fullname");}
    public String getDepartment(){return getString("department");}
    public String getPosition(){return getString("position");}
    public String getLevel(){return getString("userType");}
    public void setfullname(String fullname){put("fullname",fullname);}
    public void setDept(String dept){put("department",dept);}
    public void setPosition(String pos){put("position",pos);}
    public void setLevel(String level){put("userType",level);}
    public void setUsername(String username){put("username",username);}
    public String getId(){return getObjectId();}
    public void setDemotedby(String name){put("Demotedby",name);}
    public String getDemotion(){return getString("Demotedby");}
    public void setPromotedby(String name){put("Promotedby",name);}
    public String getPromotion(){return getString("Promotedby");}
    public void setPromotionDate(String name){put("PromotionDate",name);}
    public String getPromotionDate(){return getString("PromotionDate");}
    public void setDemotionDate(String name){put("DemotionDate",name);}
    public String getDemotionDate(){return getString("DemotionDate");}

}
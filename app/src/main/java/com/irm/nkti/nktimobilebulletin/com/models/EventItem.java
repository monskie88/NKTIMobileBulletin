package com.irm.nkti.nktimobilebulletin.com.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by MONSKIE on 3/24/2015.
 */
@ParseClassName("Events")
public class EventItem extends ParseObject {
    public EventItem(){

    }

    public  String getUsername(){return getString("username"); }
    public String getTimePosted(){return getString("timePosted"); }
    public  String getStrPost(){return getString("content"); }
    public void setUsername(String username){put("username",username);}
    public void setContent(String content){put("content",content);}
    public void setTime(String time){put("timePosted", time);}
    public ParseFile getUserPhoto(){return getParseFile("userPhoto");}
    public void setUserPhoto(ParseFile photo){put("userPhoto",photo);}
    public String getId(){return getObjectId();}
    public void setFeedPhoto(ParseFile file){put("feedPhoto",file);}
    public ParseFile getFeedPhoto(){return getParseFile("feedPhoto");}
    public void setWeek(int i){ put("week",i);}
    public void setApprovedBy(String name){put("Approvedby",name);}
    public String getApprove(){return getString("Approvedby");}
    public void setDepartment(String dept){put("dept",dept);}
    public  String getDepartment(){return getString("dept"); }
    public  String getContent(){return getString("content"); }
    public int getDelegates(){return getInt("noofdelegates");}
    public void setDelegates(int number){put("noofdelegates",number);}
}


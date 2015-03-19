package com.irm.nkti.nktimobilebulletin.com.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by MONSKIE on 3/19/2015.
 */
@ParseClassName("PendingFeed")
public class PendingFeed extends ParseObject {
    public PendingFeed(){


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
    public void setAttachment(int i){put("isAttachment",i);}
    public int getAttachment(){return getInt("isAttachment");}
    public void setWeek(int i){ put("week",i);}

}


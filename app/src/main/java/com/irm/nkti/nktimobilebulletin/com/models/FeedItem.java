package com.irm.nkti.nktimobilebulletin.com.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.sql.Date;

/**
 * Created by MONSKIE on 2/3/2015.
 */
@ParseClassName("FeedItems")
public class FeedItem extends ParseObject{
    private String userName,timePosted,strPost;
    public FeedItem(){


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
    public void setApprovedBy(String name){put("Approvedby",name);}
    public String getApprove(){return getString("Approvedby");}

}

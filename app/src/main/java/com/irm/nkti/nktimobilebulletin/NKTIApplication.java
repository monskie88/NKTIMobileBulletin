package com.irm.nkti.nktimobilebulletin;

import android.app.Application;

import com.irm.nkti.nktimobilebulletin.com.models.AdminList;
import com.irm.nkti.nktimobilebulletin.com.models.EventItem;
import com.irm.nkti.nktimobilebulletin.com.models.FeedItem;
import com.irm.nkti.nktimobilebulletin.com.models.PendingFeed;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by MONSKIE on 2/3/2015.
 */
public class NKTIApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();


		/*
		 * In this tutorial, we'll subclass ParseObject for convenience to
		 * create and modify Meal objects
		 */
        ParseObject.registerSubclass(FeedItem.class);
        ParseObject.registerSubclass(AdminList.class);
        ParseObject.registerSubclass(PendingFeed.class);
        ParseObject.registerSubclass(EventItem.class);
        Parse.initialize(this, "VRsvlABLhL1pm8zqcbuKS3RZxOTMfStrbkvVd6An", "1QONJCeJEKa9nFqyh3kn9z2m1EjfRlI1DZG4RIyr");

		/*
		 * Fill in this section with your Parse credentials
		 */


		/*
		 * This app lets an anonymous user create and save photos of meals
		 * they've eaten. An anonymous user is a user that can be created
		 * without a username and password but still has all of the same
		 * capabilities as any other ParseUser.
		 *
		 * After logging out, an anonymous user is abandoned, and its data is no
		 * longer accessible. In your own app, you can convert anonymous users
		 * to regular users so that data persists.
		 *
		 * Learn more about the ParseUser class:
		 * https://www.parse.com/docs/android_guide#users
		 */
       /* ParseUser.enableAutomaticUser();

		*//*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 *//*
        ParseACL defaultACL = new ParseACL();

		*//*
		 * If you would like all objects to be private by default, remove this
		 * line
		 *//*
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);*/

    }
}

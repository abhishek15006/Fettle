package fettle.iiitd.com.fettle;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class Pushreceiver extends ParsePushBroadcastReceiver {
    String TAG = "MA_push";

    SharedPreferences sett = null;

    public void sendNotification(Intent resultIntent, String message, Context context) throws Exception {
        int mNotificationId = 1;


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)//ic_launcher)
                .setContentTitle("Fettle Notification")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.BLUE, 700, 2200)
                .setVibrate(new long[]{300, 300, 300, 300})
                .setOnlyAlertOnce(true)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void onPushReceive(Context context, Intent intent) {

        Bundle extra = intent.getExtras();
        String json = extra.getString("com.parse.Data");
        JSONObject jo = null;
        String message = null;
        String objectID = null;
        String type = null;

        try {
            jo = new JSONObject(json);
            type = jo.getString("type");
            message = jo.getString("message");
            objectID = jo.getString("objectID" +
                    "" +
                    "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParseUser user = checkAndGetUser(context);


        //TODO not supporting multiple push for now
        try {
            if (type != null && user != null && objectID != null) {
                Intent myIntent = null;

                ParseObject.unpinAll("push"); //not supporting multiple push for now
                myIntent = new Intent("fettle.iiitd.com.fettle.Activities.LandingActivity");


                if (myIntent != null) {
                    ParseAnalytics.trackAppOpenedInBackground(myIntent);
                    myIntent.putExtra("push", true);
                    sendNotification(myIntent, message, context);
                }
            }
        } catch (Exception e) {

        }

        super.onPushReceive(context, intent);

    }

    private ParseUser checkAndGetUser(Context context) {

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
//			Constants.setCrashUser(user);
            return user;
        }
        String googleId = "";
        try {
            Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
            googleId += accounts[0].name;
        } catch (Exception e) {
        }
        try {
            user = ParseUser.logIn(googleId, "password");
//			Constants.setCrashUser(user);
            return user;
        } catch (ParseException e) {
//			Constants.setCrashData("PushUserError", "both getCurrentUser and logIn failed    "+e.getLocalizedMessage());
            e.printStackTrace();
        }

        return null;
    }


}


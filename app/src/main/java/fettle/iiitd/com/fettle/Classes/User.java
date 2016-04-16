package fettle.iiitd.com.fettle.Classes;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manan on 05-04-2016.
 */
public class User {

    private static final String TAG = "UserClass";

    public static float getBmi() {
        return Utils.getBmi(getHeight(), getWeight());
    }

    public static int getHeight() {
        return ParseUser.getCurrentUser().getInt("height");
    }

    public static int getWeight() {
        return ParseUser.getCurrentUser().getInt("weight");
    }

    public static int getTargetWeight() {
        return ParseUser.getCurrentUser().getInt("weight_target");
    }

    public static int getDailyCalorieIntake(Context context) {
        return Utils.getPref(context, Utils.DAILY_CALORIE_KEY);
    }

    public static boolean isMale() {
        return ParseUser.getCurrentUser().getBoolean("male");
    }

    public static String getLifestyle() {
        return ParseUser.getCurrentUser().getString("active");
    }

    public static String getName() {
        return ParseUser.getCurrentUser().getString("name");
    }

    public static String getEmail() {
        return ParseUser.getCurrentUser().getUsername();
    }

    public static String getExercise1() {
        return ParseUser.getCurrentUser().getString("exercise1");
    }

    public static String getExercise2() {
        return ParseUser.getCurrentUser().getString("exercise2");
    }

    public static List<Utils.BmiDate> getPastBmis() {
        List<Utils.BmiDate> bmis = new ArrayList<>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Track");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.orderByAscending("CreatedAt");
        List<ParseObject> lPo = new ArrayList<>();
        try {
            lPo = query.find();
            try {
                ParseObject.unpinAll("bmis");
            } catch (Exception e) {
            }
            ParseObject.pinAllInBackground("bmis", lPo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (ParseObject po : lPo) {
            try {
//                bmis.add(new Utils.BmiDate(po.getCreatedAt(), Utils.getBmi(po.getInt("height"), po.getInt("weight"))));
                bmis.add(new Utils.BmiDate(po.getDate("CreatedAt"), Utils.getBmi(po.getInt("height"), po.getInt("weight"))));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return bmis;
    }

    public static List<Utils.BmiDate> getPastBmisCached() {
        List<Utils.BmiDate> bmis = new ArrayList<>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Track");
        query.fromLocalDatastore();
        query.fromPin("bmis");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.orderByAscending("CreatedAt");
        List<ParseObject> lPo = new ArrayList<>();
        try {
            lPo = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (ParseObject po : lPo) {
            try {
//                bmis.add(new Utils.BmiDate(po.getCreatedAt(), Utils.getBmi(po.getInt("height"), po.getInt("weight"))));
                bmis.add(new Utils.BmiDate(po.getDate("CreatedAt"), Utils.getBmi(po.getInt("height"), po.getInt("weight"))));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return bmis;
    }


}

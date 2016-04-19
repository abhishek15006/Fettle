package fettle.iiitd.com.fettle.Classes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fettle.iiitd.com.fettle.Activities.LandingActivity;
import fettle.iiitd.com.fettle.R;

/**
 * Created by Manan on 31-03-2016.
 */
public class Utils {

    public static final String DAILY_CALORIE_KEY = "dailyCalorie";
    public static final String WALK_10_CALORIES_KEY = "walkTen";
    public static final String RUN_10_CALORIES_KEY = "runTen";
    public static final String STAIRS_10_CALORIES_KEY = "stairsTen";
    public static final String BICYCLE_10_CALORIES_KEY = "bicycleTen";
    public static final String PREF_NAME = "Pref";
    public static final String EXERCISE_PIN = "exerciseToday";

    public static float getBmi(int heightInCm, int weight) {
        float height = heightInCm / 100f;
        float bmi = 0f;
        if (weight == 0)
            return 0;
        bmi = (weight * 1f) / (height * height * 1f);
        return bmi;
    }

    public static int getExerciseImageId(String exercise, boolean first) {
        Map<String, Integer> map = new HashMap<>();
        map.put("Walk", R.drawable.walking_o);
        map.put("Jog", R.drawable.running_o);
        map.put("Stairs", R.drawable.stairs_o);
        map.put("Bicycle", R.drawable.cycling_o);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("Walk", R.drawable.walking);
        map2.put("Jog", R.drawable.running);
        map2.put("Stairs", R.drawable.stairs);
        map2.put("Bicycle", R.drawable.cycling);

        if (first)
            return map.get(exercise);
        else
            return map2.get(exercise);
    }

    public static int getTimeForExercise(Context context, String exercise, int calories) {
        Map<String, String> map = new HashMap<>();
        map.put("Walk", Utils.WALK_10_CALORIES_KEY);
        map.put("Jog", Utils.RUN_10_CALORIES_KEY);
        map.put("Stairs", Utils.STAIRS_10_CALORIES_KEY);
        map.put("Bicycle", Utils.BICYCLE_10_CALORIES_KEY);

        int exercise10 = Utils.getPref(context, map.get(exercise));

        return ((calories * exercise10) / 10) / 60;
    }

    /**
     * Target Calories to target weight mapping
     *
     * @param age    age in years
     * @param weight weight in kg
     * @param height height in cm
     * @param sex    "m" or "f"
     * @return HashMap of target to calories required
     */
    public static HashMap<String, String> CalTarget(int age, int weight, int height, String sex) {
        String url = "http://www.calculator.net/calorie-calculator.html?ctype=metric&cage="
                + age + "&csex=" + sex + "&cheightm=" + height + "&ckg=" + weight;
        HashMap<String, String> CalTarget = new HashMap<String, String>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table").get(6);
            for (Element row : table.select("tr")) {
                Elements tds = row.select("td");
                if (tds.text().contains("maintain your weight"))
                    CalTarget.put("maintain_weight", tds.get(1).text());
                else if (tds.text().contains("lose 0.5 kg"))
                    CalTarget.put("lose_0.5kg", tds.get(1).text());
                else if (tds.text().contains("lose 1 kg"))
                    CalTarget.put("lose_1kg", tds.get(1).text());
                else if (tds.text().contains("gain 0.5"))
                    CalTarget.put("gain_0.5kg", tds.get(1).text());
                else if (tds.text().contains("gain 1"))
                    CalTarget.put("gain_1kg", tds.get(1).text());
            }

            System.out.println(CalTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CalTarget;
    }

    /**
     * Convert Calories into Activities:<br>
     * walk: Walking, 3.0 mph, mod. pace, walking dog<br>
     * run: Running,general<br>
     * stairs: Walking, upstairs<br>
     * cycling: Bicycling, <10mph, leisure<br>
     *
     * @param weight weight in kg
     * @param energy energy in calories
     * @return HashMap with keys Walk, Stairs, Bicycle, Run and corresponding time
     */
    public static HashMap<String, String> CaloriesToActivity(int weight, int energy) {
        String url = "http://www.healthassist.net/calories/act-list.php";
        url = url + "?" + weight + "," + energy + "," + "false";
        HashMap<String, String> ActivityToTime = new HashMap<String, String>();
        String table_id = "sport";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements table = doc.select("table#" + table_id);

            for (Element row : table.select("tr")) {
                Elements tds = row.select("td");
                if (tds.size() > 0 && tds.get(0).text().equals("Walking, 3.0 mph, mod. pace, walking dog"))
                    ActivityToTime.put("Walk", tds.get(1).text());
                else if (tds.size() > 0 && tds.get(0).text().equals("Running, general"))
                    ActivityToTime.put("Run", tds.get(1).text());
                else if (tds.size() > 0 && tds.get(0).text().equals("Walking, upstairs"))
                    ActivityToTime.put("Stairs", tds.get(1).text());
                else if (tds.size() > 0 && tds.get(0).text().equals("Bicycling, <10mph, leisure"))
                    ActivityToTime.put("Bicycle", tds.get(1).text());
            }
            System.out.println(ActivityToTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ActivityToTime;
    }

    public static void storePref(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getPref(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    public static String toTitleCase(String input) {
        try {
            StringBuilder titleCase = new StringBuilder();
            boolean nextTitleCase = true;
            for (char c : input.toCharArray()) {
                if (Character.isSpaceChar(c)) {
                    nextTitleCase = true;
                } else if (nextTitleCase) {
                    c = Character.toTitleCase(c);
                    nextTitleCase = false;
                }
                titleCase.append(c);
            }
            return titleCase.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    public static int convertDurationToCalories(Context context, String exercise, int durationMin) {
        Map<String, String> map = new HashMap<>();
        map.put("Walk", Utils.WALK_10_CALORIES_KEY);
        map.put("Jog", Utils.RUN_10_CALORIES_KEY);
        map.put("Stairs", Utils.STAIRS_10_CALORIES_KEY);
        map.put("Bicycle", Utils.BICYCLE_10_CALORIES_KEY);

        float exercise10 = Utils.getPref(context, map.get(exercise));

        return (int) ((10f / exercise10) * (float) durationMin * 60f);
    }

    public static void sendNotification(Context context) throws Exception {
        int mNotificationId = 1;
        String message = "You forgot to add your meal.";

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo_small)//ic_launcher)
                .setContentTitle("Fettle")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.BLUE, 700, 2200)
                .setVibrate(new long[]{300, 300, 300, 300})
                .setOnlyAlertOnce(true)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Intent intent = new Intent(context, LandingActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static class BmiDate {
        public Date date;
        public float bmi;

        public BmiDate(Date date, float bmi) {
            this.date = date;
            this.bmi = bmi;
        }

        public BmiDate() {
        }

        @Override
        public String toString() {
            return "BmiDate{" +
                    "date=" + date +
                    ", bmi=" + bmi +
                    '}';
        }
    }

    public static class FoodCategory {
        public String name;

        public FoodCategory(String name) {
            this.name = name;
        }

        public FoodCategory() {
        }

        @Override
        public String toString() {
            return "FoodCategory{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }


}

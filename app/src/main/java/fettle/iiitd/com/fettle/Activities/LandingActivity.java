package fettle.iiitd.com.fettle.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fettle.iiitd.com.fettle.Classes.Dish;
import fettle.iiitd.com.fettle.Classes.User;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.Fragments.CardIntakeFragment;
import fettle.iiitd.com.fettle.Fragments.CardIntakeNutrientFragment;
import fettle.iiitd.com.fettle.Fragments.CardTrackerFragment;
import fettle.iiitd.com.fettle.Fragments.CardTrackerWeekFragment;
import fettle.iiitd.com.fettle.R;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NUM_PAGES1 = 2;
    private static final int NUM_PAGES2 = 2;
    private static final String TAG = "LandingActivity";
    public static boolean updateData = false;
    public static List<Dish> moreDishes = new ArrayList<>();
    public static boolean added1 = false;
    public static boolean added2 = false;
    public static boolean added3 = false;
    public static boolean added4 = false;
    public static String meal = "Breakfast";
    public static GoogleApiClient mGoogleApiClient;
    private ViewPager mPager1;
    private PagerAdapter mPagerAdapter1;
    private ViewPager mPager2;
    private PagerAdapter mPagerAdapter2;
    private Fragment fragment1, fragment2;
    private CardIntakeFragment fragment3;
    private CardIntakeNutrientFragment fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


//        FloatingActionButton order = (FloatingActionButton) findViewById(R.id.fab);
//        order.setOnClickListener(this);

        mPager1 = (ViewPager) findViewById(R.id.pager1);
        mPagerAdapter1 = new ScreenSlidePagerAdapter1(getSupportFragmentManager());
        mPager1.setAdapter(mPagerAdapter1);

        CirclePageIndicator indicator1 = (CirclePageIndicator) findViewById(R.id.indicator1);
        indicator1.setViewPager(mPager1);

        mPager2 = (ViewPager) findViewById(R.id.pager2);
        mPagerAdapter2 = new ScreenSlidePagerAdapter2(getSupportFragmentManager());
        mPager2.setAdapter(mPagerAdapter2);

        CirclePageIndicator indicator2 = (CirclePageIndicator) findViewById(R.id.indicator2);
        indicator2.setViewPager(mPager2);

        setNavigationDrawer(toolbar);

        ((TextView) findViewById(R.id.tvHeight)).setText(ParseUser.getCurrentUser().getInt("height") + "cm");
        findViewById(R.id.tvHeight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, ProfileInput.class));
            }
        });
        ((TextView) findViewById(R.id.tvWeight)).setText(ParseUser.getCurrentUser().getInt("weight") + "kg");
        findViewById(R.id.tvWeight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingActivity.this, ProfileInput.class));
            }
        });

        buildFitnessClient();


        List<Object> fitlog = new ArrayList<>();
        GoogleFit googlefit = new GoogleFit(mGoogleApiClient);
        if (googlefit.getFitnessData() != null) {
            fitlog.addAll(googlefit.getFitnessData());
            Log.d("mclient", fitlog.toString());
        } else {
            Log.d("mclient", "null");
        }

        /*TODO you get stepdata class object and exercisedata class object in fitlog arraylist.
        Check if it shows correctly,I would have checked but there is no data on mine ,
        however I did check it on the sample application on your phone*/








        //for testing new screens
        /*Intent myIntent = new Intent(LandingActivity.this, HomeScreen.class);
        startActivity(myIntent);*/

//        (new Handler()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                downloadData(true);
//            }
//        }, 5000);

//        Log.d(TAG, User.getPastBmis().toString());

    }

    private void buildFitnessClient() {
        // Create the Google API Client1
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle) {
                                Log.d(TAG, "Connected!!!");
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.d(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.d(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "Google Play services connection failed. Cause: " +
                                result.toString());
                    }
                })
                .build();
        Log.d("ac", "dd");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.fab) {
            Intent myIntent = new Intent(LandingActivity.this, FoodTypeSelection.class);
            startActivity(myIntent);
        }
        if (v.getId() == R.id.fab1) {
            ((FloatingActionMenu) (findViewById(R.id.fab))).close(true);
            final String[] items = {"Breakfast", "Lunch", "Dinner"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select meal");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    meal = items[which];
                    Intent myIntent = new Intent(LandingActivity.this, FoodOrderCategory.class);
                    startActivity(myIntent);
                    dialog.dismiss();
                    ;
                }
            });
            builder.create().show();

        }
        if (v.getId() == R.id.fab2) {
            ((FloatingActionMenu) (findViewById(R.id.fab))).close(true);
            Intent myIntent = new Intent(LandingActivity.this, AddFoodActivity.class);
            startActivity(myIntent);

        }

    }

    public void setNavigationDrawer(Toolbar toolbar) {
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName(User.getName()).withEmail(User.getEmail()).withIcon(getResources().getDrawable(R.drawable.female_avatar))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectable(false),
                        new DividerDrawerItem(),
                        new ExpandableDrawerItem().withName("My Profile").withIcon(GoogleMaterial.Icon.gmd_account).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("My Orders").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_shopping_cart).withIdentifier(2000),
                                new SecondaryDrawerItem().withName("My Statistics").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_chart).withIdentifier(2001)
                        ),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Rate Us").withIcon(GoogleMaterial.Icon.gmd_star).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("Report").withIcon(GoogleMaterial.Icon.gmd_block).withIdentifier(1).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 4) {
                            Intent myIntent = new Intent(LandingActivity.this, CustomCalendar.class);
                            startActivity(myIntent);
                        } else if (position == 5) {
                            Intent myIntent = new Intent(LandingActivity.this, ProfileInfo.class);
                            startActivity(myIntent);
                        }
//                        Toast.makeText(LandingActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .build();

        result.addStickyFooterItem(new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_settings).withName("Settings"));
    }

    public void updateMeals(List<Dish> lDish) {

        int breakfast = 0;
        int lunch = 0;
        int dinner = 0;
        int fiber = 0;
        int fats = 0;
        int carbs = 0;
        int proteins = 0;

        for (Dish each : lDish) {
            float multiplier = 1f;
            if (each.getDescription().toLowerCase().startsWith("gram")) {
                try {
                    multiplier = each.getQuantity() / Float.parseFloat(each.getGram());
                } catch (Exception e) {
                }
            } else {
                try {
                    multiplier = each.getQuantity() / Float.parseFloat(each.getMeasure());
                } catch (Exception e) {
                }
            }

            if (each.getMeal().equals("Breakfast")) {
                try {
                    breakfast += Float.parseFloat(each.getCalories()) * multiplier;
                } catch (Exception e) {
                }
            } else if (each.getMeal().equals("Lunch")) {
                try {
                    lunch += Float.parseFloat(each.getCalories()) * multiplier;
                } catch (Exception e) {
                }
            } else if (each.getMeal().equals("Dinner")) {
                try {
                    dinner += Float.parseFloat(each.getCalories()) * multiplier;
                } catch (Exception e) {
                }
            }

            try {
                fiber += Float.parseFloat(each.getFiber()) * multiplier;
            } catch (Exception e) {
            }
            try {
                fats += Float.parseFloat(each.getFat()) * multiplier;
            } catch (Exception e) {
            }
            try {
                carbs += Float.parseFloat(each.getCarbs()) * multiplier;
            } catch (Exception e) {
            }
            try {
                proteins += Float.parseFloat(each.getProtein()) * multiplier;
            } catch (Exception e) {
            }

        }

        fragment3.updateCalories(breakfast, lunch, dinner, Utils.getPref(this, Utils.DAILY_CALORIE_KEY));
        fragment4.updateNutrients(fiber, fats, carbs, proteins);
    }

    private ParseQuery<ParseObject> getParseQueryForFoodDownload() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        Date startDate = calendar.getTime();
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("FoodIntake");
        parseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        parseQuery.whereGreaterThan("createdAt", startDate);

        return parseQuery;
    }

    public void getCachedData(final boolean update) {
        List<ParseObject> lPo = new ArrayList<>();
        final List<Dish> lDish = new ArrayList<>();
        ParseQuery<ParseObject> parseQuery = getParseQueryForFoodDownload();
        parseQuery.fromLocalDatastore();
        parseQuery.fromPin();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject each : objects) {
                        lDish.add(new Dish(each));
                    }
                    if (update)
                        updateMeals(lDish);
                    moreDishes.clear();
                    moreDishes.addAll(lDish);
                    Log.d(TAG, "cached data done");
                    downloadData(true);
                }
            }
        });
    }

    public void downloadData(final boolean update) {

        List<ParseObject> lPo = new ArrayList<>();
        final List<Dish> lDish = new ArrayList<>();
        ParseQuery<ParseObject> parseQuery = getParseQueryForFoodDownload();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {
                        ParseObject.unpinAll("today");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    ParseObject.pinAllInBackground("today", objects);
                    for (ParseObject each : objects) {
                        lDish.add(new Dish(each));
                    }
                    if (update)
                        updateMeals(lDish);
                    moreDishes.clear();
                    moreDishes.addAll(lDish);
                    Log.d(TAG, "download data done");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if (updateData) {
            updateData = false;
            downloadData(true);
        }
        ((TextView) findViewById(R.id.tvHeight)).setText(ParseUser.getCurrentUser().getInt("height") + "cm");
        ((TextView) findViewById(R.id.tvWeight)).setText(ParseUser.getCurrentUser().getInt("weight") + "kg");
        super.onResume();
    }

    public interface AddedListener {
        public void isAdded(boolean added);
    }

    private class ScreenSlidePagerAdapter1 extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                fragment1 = new CardTrackerFragment();
                return fragment1;
            } else {
                fragment2 = new CardTrackerWeekFragment();
                return fragment2;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES1;
        }
    }

    private class ScreenSlidePagerAdapter2 extends FragmentStatePagerAdapter implements AddedListener {
        public ScreenSlidePagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                fragment3 = new CardIntakeFragment(this);
                return fragment3;
            } else {
                fragment4 = new CardIntakeNutrientFragment(this);
                return fragment4;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES2;
        }

        @Override
        public void isAdded(boolean added) {
            if (!added)
                return;
            if (added3 && added4) {
                added3 = false;
                added4 = false;
                getCachedData(true);
            }
        }
    }


}

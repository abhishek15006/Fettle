package fettle.iiitd.com.fettle.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fettle.iiitd.com.fettle.Activities.LandingActivity;
import fettle.iiitd.com.fettle.Activities.RecommendationActivity;
import fettle.iiitd.com.fettle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardIntakeFragment extends Fragment {

    String[] nutrients = {"fibers", "fats", "carbohydrates", "proteins"};
    int[] nutrientDrawables = {R.drawable.fiber_g, R.drawable.fats_g, R.drawable.carbs_g, R.drawable.protein_g};
    private LandingActivity.AddedListener mAddedListener;
    private String deficientNutrient = "";

    public CardIntakeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CardIntakeFragment(LandingActivity.AddedListener mAddedListener) {
        this.mAddedListener = mAddedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_intake, container, false);
    }

    public void setDeficiencyData() {
        final int prevNumDaysData = 2;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DATE, -prevNumDaysData);
        Date startDate = calendar.getTime();
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("FoodIntake");
        parseQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        parseQuery.whereGreaterThan("CreatedAt", startDate);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //check if prevNumDaysData exists
                    boolean foundPrev = false;
                    for (ParseObject each : objects) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.add(Calendar.DATE, -(prevNumDaysData - 1));
                        Date datePrevPlus1 = calendar.getTime();
//                        if (each.getCreatedAt().compareTo(datePrevPlus1) < 0)
                        if (each.getDate("CreatedAt").compareTo(datePrevPlus1) < 0)
                            foundPrev = true;
                    }
                    if (!foundPrev)
                        return;

                    int fiber = 0;
                    int fats = 0;
                    int carbs = 0;
                    int proteins = 0;
                    for (ParseObject each : objects) {
                        fiber += Float.parseFloat(each.getString("fiber"));
                        fats += Float.parseFloat(each.getString("fat"));
                        carbs += Float.parseFloat(each.getString("carb"));
                        proteins += Float.parseFloat(each.getString("protein"));
                    }
                    int deficiency = -1;
                    if (fiber < 3 * CardIntakeNutrientFragment.limitFiber) {
                        deficiency = 0;
                    } else if (fats < 3 * CardIntakeNutrientFragment.limitFats) {
                        deficiency = 1;
                    } else if (carbs < 3 * CardIntakeNutrientFragment.limitCarbs) {
                        deficiency = 2;
                    } else if (proteins < 3 * CardIntakeNutrientFragment.limitProteins) {
                        deficiency = 3;
                    }

                    try {
                        if (deficiency == -1) {
                            ((TextView) getActivity().findViewById(R.id.tvRequirement)).setText("Your intake is perfectly alright.");
                            ((ImageView) getActivity().findViewById(R.id.imRequirement)).setImageResource(android.R.color.transparent);
                        } else {
                            deficientNutrient = nutrients[deficiency];
                            ((TextView) getActivity().findViewById(R.id.tvRequirement)).setText("You are running low on " + nutrients[deficiency]);
                            ((ImageView) getActivity().findViewById(R.id.imRequirement)).setImageResource(nutrientDrawables[deficiency]);
                        }
                    } catch (Exception ex) {

                    }

                }
            }
        });
    }

    public void updateCalories(int breakfast, int lunch, int dinner, int required) {
        TextView tv;
        tv = (TextView) getActivity().findViewById(R.id.tvBreakfast);
        tv.setText(breakfast == 0 ? "..." : breakfast + "");
        tv = (TextView) getActivity().findViewById(R.id.tvLunch);
        tv.setText(lunch == 0 ? "..." : lunch + "");
        tv = (TextView) getActivity().findViewById(R.id.tvDinner);
        tv.setText(dinner == 0 ? "..." : dinner + "");
        tv = (TextView) getActivity().findViewById(R.id.tvTotal);
        int total = breakfast + lunch + dinner;
        tv.setText(total + "");
        float percentage = total * 100 / required;
        int percentageInt = (int) percentage;
        tv = (TextView) getActivity().findViewById(R.id.tvPercentage);
        tv.setText(percentageInt + "%");
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressIntake);
        progressBar.setProgress(percentageInt);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LandingActivity.added4 = true;
        mAddedListener.isAdded(true);
        setDeficiencyData();
        getActivity().findViewById(R.id.llRecommendation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deficientNutrient.equals(""))
                    return;
                Intent myIntent = new Intent(getActivity(), RecommendationActivity.class);
                myIntent.putExtra("nutrient", deficientNutrient);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStop() {
        LandingActivity.added4 = false;
        mAddedListener.isAdded(false);
        super.onStop();
    }
}

package fettle.iiitd.com.fettle.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

import fettle.iiitd.com.fettle.Activities.DayOverview;
import fettle.iiitd.com.fettle.Activities.LandingActivity;
import fettle.iiitd.com.fettle.Classes.User;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardTrackerWeekFragment extends Fragment implements View.OnClickListener {

    private int value1 = 5;
    private int value2 = 7;
    private String[] days = {"M", "T", "W", "T", "F", "Sa", "Su"};
    private LinearLayout[] lls = new LinearLayout[7];
    private LandingActivity.AddedListener mAddedListener;

    public CardTrackerWeekFragment() {
        // Required empty public constructor
    }

    public CardTrackerWeekFragment(LandingActivity.AddedListener addedListener) {
        // Required empty public constructor
        this.mAddedListener = addedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_tracker_week, container, false);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.linearLayout);
        ll.setWeightSum(7);
        for (int i = 0; i < 7; i++) {
            View v = inflater.inflate(R.layout.landing_daily_fitness_tracker_subpart, null, false);

            ImageView im = (ImageView) v.findViewById(R.id.imExercise1);
            im.setImageResource(Utils.getExerciseImageId(User.getExercise1(), true));

            im = (ImageView) v.findViewById(R.id.imExercise2);
            im.setImageResource(Utils.getExerciseImageId(User.getExercise2(), false));

            lls[i] = (LinearLayout) v;
            v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            TextView tv = (TextView) v.findViewById(R.id.tvDay);
            tv.setText(days[i]);
            Calendar calendar = Calendar.getInstance();
            if (i == (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7)
                tv.setBackgroundResource(R.drawable.circular_background_yellow);

            ProgressBar mProgress1 = (ProgressBar) v.findViewById(R.id.circularProgressbar1);
            mProgress1.setMax(100); // Maximum Progress
            mProgress1.setProgress(0);   // Main Progress
            mProgress1.setSecondaryProgress(0); // Secondary Progress

            ProgressBar mProgress2 = (ProgressBar) v.findViewById(R.id.circularProgressbar2);
            mProgress2.setMax(100); // Maximum Progress
            mProgress2.setProgress(0);   // Main Progress
            mProgress2.setSecondaryProgress(0); // Secondary Progress

            ll.addView(v);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.more_tracker_week).setOnClickListener(this);

        LandingActivity.added2 = true;
        mAddedListener.isAdded(true);

    }

    //TODO Danish use these 2 functions to set progresses or modify accordingly. u can use fragment 4 to call this function from Landing activity
    //TODO Danish, ask Sarthak how to fill progress(percentage) here

    /**
     * Week starts from Monday => 0 is Monday
     *
     * @param progressesExercise1
     * @param progressesExercise2
     */
    public void setProgress(int[] progressesExercise1, int[] progressesExercise2) {
        for (int i = 0; i < 7; i++) {
            setProgress(i, progressesExercise1[i], progressesExercise2[i]);
        }
    }

    public void setProgress(int day, int progress1, int progress2) {
        ((ProgressBar) lls[day].findViewById(R.id.circularProgressbar1)).setProgress(progress1);
        ((ProgressBar) lls[day].findViewById(R.id.circularProgressbar2)).setProgress(progress2);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.more_tracker_week) {
            Intent intent = new Intent(getActivity(), DayOverview.class);
            intent.putExtra("exercise", true);
            startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        LandingActivity.added2 = false;
        mAddedListener.isAdded(false);
        super.onStop();
    }

}

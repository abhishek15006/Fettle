package fettle.iiitd.com.fettle.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import fettle.iiitd.com.fettle.Activities.CustomCalendar;
import fettle.iiitd.com.fettle.Activities.LandingActivity;
import fettle.iiitd.com.fettle.Classes.Exercise;
import fettle.iiitd.com.fettle.Classes.User;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.R;

/**
 * Created by danishgoel on 11/03/16.
 */
public class ExerciesOverview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_exercise_overview, container, false);

        ((ImageView) view.findViewById(R.id.imExercise1)).setImageResource(Utils.getExerciseImageId(User.getExercise1(), true));
        ((ImageView) view.findViewById(R.id.imExercise2)).setImageResource(Utils.getExerciseImageId(User.getExercise2(), false));

        Calendar calendarSelected = Calendar.getInstance();

        List<Exercise> exercises;
        exercises = LandingActivity.allExercises;
        if (getActivity().getIntent().getBooleanExtra("calendar", false)) {
            exercises = CustomCalendar.allExercises;
            calendarSelected.setTimeInMillis(getActivity().getIntent().getLongExtra("date", calendarSelected.getTimeInMillis()));
        }

        //starts
        int exercise1Duration = 0;
        int exercise2Duration = 0;

        int exercise1Cals = 0;
        int exercise2Cals = 0;

        for (Exercise exercise : exercises) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(exercise.getDate());
            if (exercise.getExercise().equals(User.getExercise1())) {
                if (calendarSelected.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                    exercise1Cals += Utils.convertDurationToCalories(getActivity(), exercise.getExercise(), exercise.getDuration());
                    exercise1Duration += exercise.getDuration();
                }
            } else if (exercise.getExercise().equals(User.getExercise2())) {
                if (calendarSelected.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                    exercise2Cals = Utils.convertDurationToCalories(getActivity(), exercise.getExercise(), exercise.getDuration());
                    exercise2Duration += exercise.getDuration();
                }
            }
        }
        //ends
        setDataforView(exercise1Duration, exercise2Duration, exercise1Cals, exercise2Cals, exercise1Cals + exercise2Cals, (exercise1Cals + exercise2Cals) - (Utils.getPref(getActivity(), Utils.DAILY_CALORIE_KEY) / 3), view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void setDataforView(int exercise1Time, int exercise2Time, int cal1, int cal2, int totalcal, int calorieinfo, View view) {
        TextView time1, time2, tvcal1, tvcal2, totalCal, calorieInfo;
        ProgressBar progressBar;
        if (view == null) {
            time1 = (TextView) getActivity().findViewById(R.id.time1);
            time2 = (TextView) getActivity().findViewById(R.id.time2);
            tvcal1 = (TextView) getActivity().findViewById(R.id.cal1);
            tvcal2 = (TextView) getActivity().findViewById(R.id.cal2);
            totalCal = (TextView) getActivity().findViewById(R.id.totalCal);
            calorieInfo = (TextView) getActivity().findViewById(R.id.calorie_info);
            progressBar = (ProgressBar) getActivity().findViewById(R.id.pg);
        } else {
            time1 = (TextView) view.findViewById(R.id.time1);
            time2 = (TextView) view.findViewById(R.id.time2);
            tvcal1 = (TextView) view.findViewById(R.id.cal1);
            tvcal2 = (TextView) view.findViewById(R.id.cal2);
            totalCal = (TextView) view.findViewById(R.id.totalCal);
            calorieInfo = (TextView) view.findViewById(R.id.calorie_info);
            progressBar = (ProgressBar) view.findViewById(R.id.pg);
        }

        time1.setText(exercise1Time + " Min");
        time2.setText(exercise2Time + " Min");
        tvcal1.setText(cal1 + " Cal");
        tvcal2.setText(cal2 + " Cal");
        totalCal.setText(totalcal + " Cal");
        calorieInfo.setText("You were" + (calorieinfo < 0 ? " short" : " over") + " by " + Math.abs(calorieinfo) + " calories");
        progressBar.setProgress((totalcal * 100) / (Utils.getPref(getActivity(), Utils.DAILY_CALORIE_KEY) / 3));
    }
}

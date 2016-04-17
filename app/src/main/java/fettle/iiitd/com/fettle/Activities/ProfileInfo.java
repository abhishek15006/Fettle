package fettle.iiitd.com.fettle.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fettle.iiitd.com.fettle.Classes.User;
import fettle.iiitd.com.fettle.Classes.Utils;
import fettle.iiitd.com.fettle.R;
import fettle.iiitd.com.fettle.Utilities.MyYAxisValueFormatter;

/**
 * Created by danishgoel on 11/03/16.
 */
public class ProfileInfo extends AppCompatActivity implements
        OnChartValueSelectedListener, View.OnClickListener {
    private static final String TAG = "ProfileInfoActivity";
    protected BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        toolbar.setTitle("Overview");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        try {

            ParseUser user = ParseUser.getCurrentUser();
            ((TextView) findViewById(R.id.weight)).setText(User.getWeight() + "kg");
            ((TextView) findViewById(R.id.height)).setText(User.getHeight() + "cm");
            ((TextView) findViewById(R.id.bmi)).setText((new DecimalFormat("#.##")).format(User.getBmi()) + "");
            ((TextView) findViewById(R.id.name)).setText(User.getName());

            ((TextView) findViewById(R.id.lifestyle)).setText(User.getLifestyle());
            if (user.getBoolean("male")) {
                ((TextView) findViewById(R.id.gender)).setText("Male");
            } else {
                ((TextView) findViewById(R.id.gender)).setText("Female");
            }
        } catch (Exception e) {

        }

        ImageView edit = (ImageView) findViewById(R.id.edit);
        edit.setOnClickListener(this);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("Date-BMI graph");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        setData(User.getPastBmisCached());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setData(User.getPastBmis());
            }
        }, 1000);


        // mChart.setDrawLegend(false);

    }

    private void setData(List<Utils.BmiDate> bmis) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < bmis.size(); i++) {
            Utils.BmiDate each = bmis.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
            xVals.add(dateFormat.format(each.date));
            yVals1.add(new BarEntry(each.bmi, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "BMI");
        set1.setBarSpacePercent(35f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
        mChart.invalidate();
        Log.d(TAG, "calendar data set");
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit) {
            Intent myIntent = new Intent(ProfileInfo.this, ProfileInput.class);
            startActivity(myIntent);
        }
    }

    @Override
    protected void onResume() {
        try {
            ((TextView) findViewById(R.id.weight)).setText(User.getWeight() + "kg");
            ((TextView) findViewById(R.id.height)).setText(User.getHeight() + "cm");
            ((TextView) findViewById(R.id.bmi)).setText((new DecimalFormat("#.##")).format(User.getBmi()) + "");
        } catch (Exception e) {

        }
        super.onResume();
    }
}

package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DistrictDataActivity extends AppCompatActivity {

    @BindView(R.id.stateStatsCardView)
    CardView stateStatsCardView;
    @BindView(R.id.stateNameTextView)
    TextView stateNameTextView;
    @BindView(R.id.stateStatsPieChart)
    PieChart stateStatsPieChart;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    @BindView(R.id.timeTextView)
    TextView timeTextView;
    @BindView(R.id.totalStateCasesTextView)
    TextView totalStateCasesTextView;
    @BindView(R.id.activeCasesTextView)
    TextView activeCasesTextView;
    @BindView(R.id.recoveredCasesTextView)
    TextView recoveredCasesTextView;
    @BindView(R.id.deceasedCasesTextView)
    TextView deceasedCasesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_data);
        ButterKnife.bind(this);

        // receiving Intent from StateDataAdapter
        String stateName = getIntent().getStringExtra("State Name");
        String confirmed = getIntent().getStringExtra("Confirmed Cases");
        String active = getIntent().getStringExtra("Active Cases");
        String recovered = getIntent().getStringExtra("Recovered");
        String deaths = getIntent().getStringExtra("Deaths");
        String lastupdatedtime = getIntent().getStringExtra("Last Time Updated");

        String[] values = lastupdatedtime.split(" ");
        String date = values[0];
        dateTextView.setText(date);
        String _24HourTime = values[1].substring(0,5);

        // converting 24 hours format to 12 hours format
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = null;
        try {
            _24HourDt = _24HourSDF.parse(_24HourTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeTextView.setText(_12HourSDF.format(Objects.requireNonNull(_24HourDt)));

        stateNameTextView.setText(stateName);
        totalStateCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(confirmed)));
        activeCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));
        recoveredCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered)));
        deceasedCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(deaths)));

        stateStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(confirmed), Color.parseColor("#fed70e")));
        stateStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(active), Color.parseColor("#56b7f1")));
        stateStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recovered), Color.parseColor("#63cbb0")));
        stateStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(deaths), Color.parseColor("#FF0000")));

        stateStatsPieChart.startAnimation();
    }
}
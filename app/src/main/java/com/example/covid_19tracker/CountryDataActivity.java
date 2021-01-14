package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryDataActivity extends AppCompatActivity {

    @BindView(R.id.flagImageView)
    ImageView flagImageView;
    @BindView(R.id.countryStatsCardView)
    CardView countryStatsCardView;
    @BindView(R.id.countryStatsPieChart)
    PieChart countryStatsPieChart;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    @BindView(R.id.timeTextView)
    TextView timeTextView;
    @BindView(R.id.countryStatsArcLoader)
    SimpleArcLoader countryStatsArcLoader;
    @BindView(R.id.cardViewConstraintLayout)
    ConstraintLayout cardViewConstraintLayout;
    @BindView(R.id.totalCasesTextView)
    TextView totalCasesTextView;
    @BindView(R.id.newTotalCasesTextView)
    TextView newTotalCasesTextView;
    @BindView(R.id.activeCasesTextView)
    TextView activeCasesTextView;
    @BindView(R.id.criticalCasesTextView)
    TextView criticalCasesTextView;
    @BindView(R.id.recoveredCasesTextView)
    TextView recoveredCasesTextView;
    @BindView(R.id.newRecoveredCasesTextView)
    TextView newRecoveredCasesTextView;
    @BindView(R.id.deceasedCasesTextView)
    TextView deceasedCasesTextView;
    @BindView(R.id.newDeceasedCasesTextView)
    TextView newDeceasedCasesTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_data);
        ButterKnife.bind(this);

        countryStatsArcLoader.start();

        // receiving Intent from CountryDataAdapter
        String flagUrl = getIntent().getStringExtra("Flag Url");
        Glide.with(this).load(flagUrl).placeholder(R.drawable.flag_icon).into(flagImageView);

        String countryName = getIntent().getStringExtra("Country Name");
        titleTextView.setText(countryName);

        String confirmed = getIntent().getStringExtra("Confirmed Cases");
        totalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(confirmed)));

        String active = getIntent().getStringExtra("Active Cases");
        activeCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));

        String recovered = getIntent().getStringExtra("Recovered");
        recoveredCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(recovered)));

        String deaths = getIntent().getStringExtra("Deaths");
        deceasedCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(deaths)));

        countryStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(confirmed), Color.parseColor("#fed70e")));
        countryStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(active), Color.parseColor("#56b7f1")));
        countryStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recovered), Color.parseColor("#63cbb0")));
        countryStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(deaths), Color.parseColor("#FF0000")));

        String todayCases = getIntent().getStringExtra("Today Cases");
        newTotalCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayCases)));

        String todayDeaths = getIntent().getStringExtra("Today Deaths");
        newDeceasedCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayDeaths)));

        String todayRecovered = getIntent().getStringExtra("Today Recovered");
        newRecoveredCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayRecovered)));

        String critical = getIntent().getStringExtra("Critical");
        criticalCasesTextView.setText(critical);

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

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                countryStatsPieChart.startAnimation();
                countryStatsArcLoader.stop();
                countryStatsArcLoader.setVisibility(View.GONE);
                flagImageView.setVisibility(View.VISIBLE);
                countryStatsCardView.setVisibility(View.VISIBLE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable,1000);
    }
}
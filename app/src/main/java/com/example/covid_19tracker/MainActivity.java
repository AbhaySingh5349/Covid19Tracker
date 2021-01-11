package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.worldStatsPieChart)
    PieChart worldStatsPieChart;
    @BindView(R.id.worldStatsArcLoader)
    SimpleArcLoader worldStatsArcLoader;
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
    @BindView(R.id.indiaDataCardView)
    CardView indiaDataCardView;
    @BindView(R.id.worldDataCardView)
    CardView worldDataCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
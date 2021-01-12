package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndiaDataActivity extends AppCompatActivity {

    @BindView(R.id.indianStatsPieChart)
    PieChart indianStatsPieChart;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    @BindView(R.id.indianStatsArcLoader)
    SimpleArcLoader indianStatsArcLoader;
    @BindView(R.id.cardViewConstraintLayout)
    ConstraintLayout cardViewConstraintLayout;
    @BindView(R.id.totalCasesTextView)
    TextView totalCasesTextView;
    @BindView(R.id.newTotalCasesTextView)
    TextView newTotalCasesTextView;
    @BindView(R.id.activeCasesTextView)
    TextView activeCasesTextView;
    @BindView(R.id.recoveredCasesTextView)
    TextView recoveredCasesTextView;
    @BindView(R.id.newRecoveredCasesTextView)
    TextView newRecoveredCasesTextView;
    @BindView(R.id.deceasedCasesTextView)
    TextView deceasedCasesTextView;
    @BindView(R.id.newDeceasedCasesTextView)
    TextView newDeceasedCasesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_data);
        ButterKnife.bind(this);
        
    //    fetchIndiaData();
    }
}
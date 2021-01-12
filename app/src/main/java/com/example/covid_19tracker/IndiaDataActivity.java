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
        
        fetchIndiaData();
    }

    private void fetchIndiaData() {
        indianStatsArcLoader.start();
        String indiaDataURL = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, indiaDataURL, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray timeSeriesJsonArray = response.getJSONArray("cases_time_series");
                    JSONObject timeSeriesJsonObject = timeSeriesJsonArray.getJSONObject(timeSeriesJsonArray.length()-1);

                    JSONArray stateWiseJsonArray = response.getJSONArray("statewise");
                    JSONObject stateWiseJsonObject = stateWiseJsonArray.getJSONObject(0);

                    String year = timeSeriesJsonObject.getString("dateymd");

                    dateTextView.setText(timeSeriesJsonObject.getString("date") + " " + year.substring(2,4));
                    totalCasesTextView.setText(stateWiseJsonObject.getString("confirmed"));
                    newTotalCasesTextView.setText("+ " + timeSeriesJsonObject.getString("dailyconfirmed"));
                    activeCasesTextView.setText(stateWiseJsonObject.getString("active"));
                    recoveredCasesTextView.setText(stateWiseJsonObject.getString("recovered"));
                    newRecoveredCasesTextView.setText("+ " + timeSeriesJsonObject.getString("dailyrecovered"));
                    deceasedCasesTextView.setText(stateWiseJsonObject.getString("deaths"));
                    newDeceasedCasesTextView.setText("+ " + timeSeriesJsonObject.getString("dailydeceased"));

                    indianStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(totalCasesTextView.getText().toString()), Color.parseColor("#fed70e")));
                    indianStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(activeCasesTextView.getText().toString()), Color.parseColor("#56b7f1")));
                    indianStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recoveredCasesTextView.getText().toString()), Color.parseColor("#63cbb0")));
                    indianStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(deceasedCasesTextView.getText().toString()), Color.parseColor("#FF0000")));

                    indianStatsPieChart.startAnimation();
                    indianStatsArcLoader.stop();
                    indianStatsArcLoader.setVisibility(View.GONE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    indianStatsArcLoader.stop();
                    indianStatsArcLoader.setVisibility(View.GONE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                indianStatsArcLoader.stop();
                indianStatsArcLoader.setVisibility(View.GONE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
                Toast.makeText(IndiaDataActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
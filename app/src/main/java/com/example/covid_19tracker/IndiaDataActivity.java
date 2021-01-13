package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndiaDataActivity extends AppCompatActivity {

    @BindView(R.id.indianStatsCardView)
    CardView indianStatsCardView;
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
    @BindView(R.id.stateDataCardView)
    CardView stateDataCardView;

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

                    String totalCases = stateWiseJsonObject.getString("confirmed");
                    String todayCases = timeSeriesJsonObject.getString("dailyconfirmed");
                    String activeCases = stateWiseJsonObject.getString("active");
                    String recoveredCases = stateWiseJsonObject.getString("recovered");
                    String newRecovered = timeSeriesJsonObject.getString("dailyrecovered");
                    String deceasedCases = stateWiseJsonObject.getString("deaths");
                    String newDeceased = timeSeriesJsonObject.getString("dailydeceased");

                    String year = timeSeriesJsonObject.getString("dateymd");

                    dateTextView.setText(timeSeriesJsonObject.getString("date") + " " + year.substring(2,4));
                    totalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(totalCases)));
                    newTotalCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayCases)));
                    activeCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(activeCases)));
                    recoveredCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(recoveredCases)));
                    newRecoveredCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(newRecovered)));
                    deceasedCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(deceasedCases)));
                    newDeceasedCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(newDeceased)));

                    indianStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(totalCases), Color.parseColor("#fed70e")));
                    indianStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(activeCases), Color.parseColor("#56b7f1")));
                    indianStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recoveredCases), Color.parseColor("#63cbb0")));
                    indianStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(deceasedCases), Color.parseColor("#FF0000")));

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            indianStatsPieChart.startAnimation();
                            indianStatsArcLoader.stop();
                            indianStatsArcLoader.setVisibility(View.GONE);
                            indianStatsCardView.setVisibility(View.VISIBLE);
                            cardViewConstraintLayout.setVisibility(View.VISIBLE);
                        }
                    };
                    handler.postDelayed(runnable,1000);

                    stateDataCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(IndiaDataActivity.this,StateListActivity.class));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    indianStatsArcLoader.stop();
                    indianStatsArcLoader.setVisibility(View.GONE);
                    indianStatsCardView.setVisibility(View.VISIBLE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(IndiaDataActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                indianStatsArcLoader.stop();
                indianStatsArcLoader.setVisibility(View.GONE);
                indianStatsCardView.setVisibility(View.VISIBLE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
                Toast.makeText(IndiaDataActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
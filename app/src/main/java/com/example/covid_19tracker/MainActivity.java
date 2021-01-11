package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

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
        
        fetchWorldData();
    }

    private void fetchWorldData() {
        worldStatsArcLoader.start();
        String worldDataURL = "https://corona.lmao.ninja/v2/all";

        StringRequest worldDataStringRequest = new StringRequest(Request.Method.GET, worldDataURL, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    totalCasesTextView.setText(jsonObject.getString("cases"));
                    newTotalCasesTextView.setText("+ " + jsonObject.getString("todayCases"));
                    activeCasesTextView.setText(jsonObject.getString("active"));
                    criticalCasesTextView.setText(jsonObject.getString("critical"));
                    recoveredCasesTextView.setText(jsonObject.getString("recovered"));
                    newRecoveredCasesTextView.setText("+ " + jsonObject.getString("todayRecovered"));
                    deceasedCasesTextView.setText(jsonObject.getString("deaths"));
                    newDeceasedCasesTextView.setText("+ " + jsonObject.getString("todayDeaths"));

                    worldStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(totalCasesTextView.getText().toString()), Color.parseColor("#fed70e")));
                    worldStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(activeCasesTextView.getText().toString()), Color.parseColor("#56b7f1")));
                    worldStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recoveredCasesTextView.getText().toString()), Color.parseColor("#63cbb0")));
                    worldStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(totalCasesTextView.getText().toString()), Color.parseColor("#FF0000")));

                    worldStatsPieChart.startAnimation();
                    worldStatsArcLoader.stop();
                    worldStatsArcLoader.setVisibility(View.GONE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    worldStatsArcLoader.stop();
                    worldStatsArcLoader.setVisibility(View.GONE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                worldStatsArcLoader.stop();
                worldStatsArcLoader.setVisibility(View.GONE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worldDataStringRequest);
    }
}
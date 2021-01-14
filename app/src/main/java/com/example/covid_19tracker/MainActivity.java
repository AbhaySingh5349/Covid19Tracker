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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.worldStatsCardView)
    CardView worldStatsCardView;
    @BindView(R.id.worldStatsPieChart)
    PieChart worldStatsPieChart;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
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

    private boolean doubleBackPressed = false;

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

                    String totalCases = jsonObject.getString("cases");
                    String todayCases = jsonObject.getString("todayCases");
                    String activeCases = jsonObject.getString("active");
                    String criticalCases = jsonObject.getString("critical");
                    String recoveredCases = jsonObject.getString("recovered");
                    String todayRecovered = jsonObject.getString("todayRecovered");
                    String deceasedCases = jsonObject.getString("deaths");
                    String newDeceased = jsonObject.getString("todayDeaths");

                    totalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(totalCases)));
                    newTotalCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayCases)));
                    activeCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(activeCases)));
                    criticalCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(criticalCases)));
                    recoveredCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(recoveredCases)));
                    newRecoveredCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(todayRecovered)));
                    deceasedCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(deceasedCases)));
                    newDeceasedCasesTextView.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(newDeceased)));

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, ''yy");
                    String dateString = formatter.format(new Date(Long.parseLong(jsonObject.getString("updated"))));
                    dateTextView.setText(dateString);

                    worldStatsPieChart.addPieSlice(new PieModel("Total Cases",Integer.parseInt(totalCases), Color.parseColor("#fed70e")));
                    worldStatsPieChart.addPieSlice(new PieModel("Active Cases",Integer.parseInt(activeCases), Color.parseColor("#56b7f1")));
                    worldStatsPieChart.addPieSlice(new PieModel("Recovered Cases",Integer.parseInt(recoveredCases), Color.parseColor("#63cbb0")));
                    worldStatsPieChart.addPieSlice(new PieModel("Deceased Cases",Integer.parseInt(deceasedCases), Color.parseColor("#FF0000")));

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            worldStatsPieChart.startAnimation();
                            worldStatsArcLoader.stop();
                            worldStatsArcLoader.setVisibility(View.GONE);
                            worldStatsCardView.setVisibility(View.VISIBLE);
                            cardViewConstraintLayout.setVisibility(View.VISIBLE);
                        }
                    };
                    handler.postDelayed(runnable,1000);

                    indiaDataCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this,IndiaDataActivity.class));
                        }
                    });

                    worldDataCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this,CountryListActivity.class));
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    worldStatsArcLoader.stop();
                    worldStatsArcLoader.setVisibility(View.GONE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                    cardViewConstraintLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                worldStatsArcLoader.stop();
                worldStatsArcLoader.setVisibility(View.GONE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
                cardViewConstraintLayout.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(worldDataStringRequest);
    }

    @Override
    public void onBackPressed() {
        //    super.onBackPressed();
        if(doubleBackPressed){
            finishAffinity();
        }else {
            doubleBackPressed = true;
            Toast.makeText(this,"Back Press again to exit the app",Toast.LENGTH_SHORT).show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackPressed = false;
                }
            },2000);
        }
    }
}
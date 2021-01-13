package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19tracker.adapter.DistrictDataAdapter;
import com.example.covid_19tracker.model.DistrictDataModelClass;
import com.example.covid_19tracker.model.StateDataModelClass;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @BindView(R.id.districtsListArcLoader)
    SimpleArcLoader districtsListArcLoader;
    @BindView(R.id.districtsRecyclerView)
    RecyclerView districtsRecyclerView;
    @BindView(R.id.districtListEditText)
    EditText districtListEditText;
    @BindView(R.id.cardView5)
    CardView cardView5;

    DistrictDataAdapter districtDataAdapter;
    List<DistrictDataModelClass> districtDataModelClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_data);
        ButterKnife.bind(this);

        districtDataModelClassList = new ArrayList<>();
        districtsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        districtDataAdapter = new DistrictDataAdapter(this,districtDataModelClassList);
        districtsRecyclerView.setAdapter(districtDataAdapter);
        fetchDistrictData();

        districtListEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchDistrict(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchDistrict(String characters) {
        List<DistrictDataModelClass> charactersList = new ArrayList<>();
        for (DistrictDataModelClass districtDataModelClass : districtDataModelClassList){
            if(districtDataModelClass.getDistrict().toLowerCase().contains(characters.toLowerCase())){
                charactersList.add(districtDataModelClass);
            }
            districtDataAdapter.searchStateList(charactersList, characters);
        }
    }

    private void fetchDistrictData(){
        districtsListArcLoader.start();
        String districtUrl = "https://api.covid19india.org/v2/state_district_wise.json";

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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, districtUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                districtDataModelClassList.clear();

                for (int i=1; i< response.length(); i++){
                    int newState = 0;
                    try {
                        JSONObject jsonObjectState = response.getJSONObject(i);

                        if(stateName.toLowerCase().equals(jsonObjectState.getString("state").toLowerCase())){
                            JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray("districtData");

                            for (int j=0; j <jsonArrayDistrict.length(); j++){
                                JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j);

                                String districtName = jsonObjectDistrict.getString("district");
                                String activeCases = jsonObjectDistrict.getString("active");
                                String deceased = jsonObjectDistrict.getString("deceased");

                                DistrictDataModelClass districtDataModelClass = new DistrictDataModelClass(districtName, activeCases, deceased);
                                districtDataModelClassList.add(districtDataModelClass);
                                districtDataAdapter.notifyDataSetChanged();
                            }
                            newState = 1;
                        }
                        if(newState==1){
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        districtsListArcLoader.stop();
                        stateStatsPieChart.startAnimation();
                        districtsListArcLoader.setVisibility(View.GONE);
                        districtsRecyclerView.setVisibility(View.VISIBLE);
                        districtListEditText.setVisibility(View.VISIBLE);
                        cardView5.setVisibility(View.VISIBLE);
                        stateStatsCardView.setVisibility(View.VISIBLE);
                    }
                };
                handler.postDelayed(runnable,1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                districtsListArcLoader.stop();
                stateStatsPieChart.startAnimation();
                districtsListArcLoader.setVisibility(View.GONE);
                districtsRecyclerView.setVisibility(View.VISIBLE);
                districtListEditText.setVisibility(View.VISIBLE);
                cardView5.setVisibility(View.VISIBLE);
                stateStatsCardView.setVisibility(View.VISIBLE);
                Toast.makeText(DistrictDataActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
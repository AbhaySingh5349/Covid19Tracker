package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19tracker.adapter.CountriesDataAdapter;
import com.example.covid_19tracker.model.CountriesDataModelClass;
import com.example.covid_19tracker.model.StateDataModelClass;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryListActivity extends AppCompatActivity {

    @BindView(R.id.countryListEditText)
    EditText countryListEditText;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.countryListArcLoader)
    SimpleArcLoader countryListArcLoader;
    @BindView(R.id.countriesRecyclerView)
    RecyclerView countriesRecyclerView;

    List<CountriesDataModelClass> countriesDataModelClassList;
    CountriesDataAdapter countriesDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        ButterKnife.bind(this);

        countriesDataModelClassList = new ArrayList<>();
        countriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        countriesDataAdapter = new CountriesDataAdapter(this,countriesDataModelClassList);
        countriesRecyclerView.setAdapter(countriesDataAdapter);

        fetchCountriesData();

        countryListEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchCountry(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchCountry(String characters) {
        List<CountriesDataModelClass> charactersList = new ArrayList<>();
        for (CountriesDataModelClass countriesDataModelClass : countriesDataModelClassList){
            if(countriesDataModelClass.getCountry().toLowerCase().contains(characters.toLowerCase())){
                charactersList.add(countriesDataModelClass);
            }
            countriesDataAdapter.searchCountryList(charactersList, characters);
        }
    }

    private void fetchCountriesData() {
        countryListArcLoader.start();
        String countriesUrl = "https://corona.lmao.ninja/v2/countries";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, countriesUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                countriesDataModelClassList.clear();

                for (int i= 0 ; i<response.length(); i++){
                    try {
                        JSONObject countryJSONObject = response.getJSONObject(i);

                        long timeStamp = Long.parseLong(countryJSONObject.getString("updated"));
                        String countryName = countryJSONObject.getString("country");
                        String cases = countryJSONObject.getString("cases");
                        String deaths = countryJSONObject.getString("deaths");
                        String recovered = countryJSONObject.getString("recovered");
                        String active = countryJSONObject.getString("active");
                        String todayCases = countryJSONObject.getString("todayCases");
                        String todayDeaths = countryJSONObject.getString("todayDeaths");
                        String todayRecovered = countryJSONObject.getString("todayRecovered");
                        String critical = countryJSONObject.getString("critical");

                        JSONObject flagJSONObject = countryJSONObject.getJSONObject("countryInfo");
                        String flagUrl = flagJSONObject.getString("flag");

                        CountriesDataModelClass countriesDataModelClass = new CountriesDataModelClass(countryName, flagUrl, cases, deaths, recovered, active, todayCases, todayDeaths, todayRecovered, critical, timeStamp);
                        countriesDataModelClassList.add(countriesDataModelClass);
                        countriesDataAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        countryListArcLoader.stop();
                        countryListArcLoader.setVisibility(View.INVISIBLE);
                        countriesRecyclerView.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.VISIBLE);
                        countryListEditText.setVisibility(View.VISIBLE);
                    }
                };
                handler.postDelayed(runnable,1000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countryListArcLoader.stop();
                countryListArcLoader.setVisibility(View.INVISIBLE);
                countriesRecyclerView.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                countryListEditText.setVisibility(View.VISIBLE);
                Toast.makeText(CountryListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
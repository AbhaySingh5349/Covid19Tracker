package com.example.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19tracker.adapter.StateDataAdapter;
import com.example.covid_19tracker.model.StateDataModelClass;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateListActivity extends AppCompatActivity {

    @BindView(R.id.stateListEditText)
    EditText stateListEditText;
    @BindView(R.id.statesRecyclerView)
    RecyclerView statesRecyclerView;
    @BindView(R.id.stateListArcLoader)
    SimpleArcLoader stateListArcLoader;

    StateDataAdapter stateDataAdapter;
    List<StateDataModelClass> stateDataModelClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_list);
        ButterKnife.bind(this);

        stateDataModelClassList = new ArrayList<>();
        statesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stateDataAdapter = new StateDataAdapter(this,stateDataModelClassList);
        statesRecyclerView.setAdapter(stateDataAdapter);

        fetchStateList();

        stateListEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchState(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchState(String characters) {
        List<StateDataModelClass> charactersList = new ArrayList<>();
        for (StateDataModelClass stateDataModelClass : stateDataModelClassList){
            if(stateDataModelClass.getState().toLowerCase().contains(characters.toLowerCase())){
                charactersList.add(stateDataModelClass);
            }
            stateDataAdapter.searchStateList(charactersList);
        }
    }

    private void fetchStateList() {
        stateListArcLoader.start();
        String indiaDataURL = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, indiaDataURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stateWiseJsonArray = response.getJSONArray("statewise");
                    stateDataModelClassList.clear();

                    for (int i=1; i< stateWiseJsonArray.length()-1; i++){
                        JSONObject jsonObject = stateWiseJsonArray.getJSONObject(i);

                        String active = jsonObject.getString("active");
                        String confirmed = jsonObject.getString("confirmed");
                        String deaths = jsonObject.getString("deaths");
                        String lastupdatedtime = jsonObject.getString("lastupdatedtime");
                        String recovered = jsonObject.getString("recovered");
                        String state = jsonObject.getString("state");

                        StateDataModelClass stateDataModelClass = new StateDataModelClass(active, confirmed, deaths, lastupdatedtime, recovered, state);
                        stateDataModelClassList.add(stateDataModelClass);
                    }

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            stateListArcLoader.stop();
                            stateListArcLoader.setVisibility(View.GONE);
                            statesRecyclerView.setVisibility(View.VISIBLE);
                        }
                    };
                    handler.postDelayed(runnable,1000);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(StateListActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    stateListArcLoader.stop();
                    statesRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StateListActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                stateListArcLoader.stop();
                statesRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
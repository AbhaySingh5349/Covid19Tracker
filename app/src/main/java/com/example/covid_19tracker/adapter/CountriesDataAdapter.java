package com.example.covid_19tracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.model.CountriesDataModelClass;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

public class CountriesDataAdapter extends RecyclerView.Adapter<CountriesDataAdapter.CountriesViewHolder> {

    private Context context;
    private List<CountriesDataModelClass> countriesDataModelClassList;

    public CountriesDataAdapter(Context context, List<CountriesDataModelClass> countriesDataModelClassList) {
        this.context = context;
        this.countriesDataModelClassList = countriesDataModelClassList;
    }

    @NonNull
    @Override
    public CountriesDataAdapter.CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.countries_display_item, parent, false);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesDataAdapter.CountriesViewHolder holder, int position) {
        CountriesDataModelClass countriesDataModelClass = countriesDataModelClassList.get(position);

        String flagUrl = countriesDataModelClass.getFlag();
        Glide.with(context).load(flagUrl).placeholder(R.drawable.flag_icon).into(holder.flagImageView);

        String countryName = countriesDataModelClass.getCountry();
        holder.countryNameTextView.setText(countryName);

        String totalCases = countriesDataModelClass.getCases();

        String activeCases = countriesDataModelClass.getActive();
        holder.countryActiveCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(activeCases)));

        String recovered = countriesDataModelClass.getRecovered();

        String deceased = countriesDataModelClass.getDeaths();

        long timestamp = countriesDataModelClass.getUpdated();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    //    holder.dateTextView.setText(simpleDateFormat.format(date));

    /*    holder.countryStatsPieChart.addPieSlice(new PieModel(String.valueOf(position),Integer.parseInt(totalCases), Color.parseColor("#fed70e")));
        holder.countryStatsPieChart.addPieSlice(new PieModel(String.valueOf(position),Integer.parseInt(activeCases), Color.parseColor("#56b7f1")));
        holder.countryStatsPieChart.addPieSlice(new PieModel(String.valueOf(position),Integer.parseInt(recovered), Color.parseColor("#63cbb0")));
        holder.countryStatsPieChart.addPieSlice(new PieModel(String.valueOf(position),Integer.parseInt(deceased), Color.parseColor("#FF0000")));

        holder.countryStatsPieChart.startAnimation(); */
    }

    @Override
    public int getItemCount() {
        return countriesDataModelClassList.size();
    }

    public static class CountriesViewHolder extends RecyclerView.ViewHolder {

        ImageView flagImageView;
        TextView countryNameTextView, countryActiveCasesTextView;

        public CountriesViewHolder(@NonNull View itemView) {
            super(itemView);

            flagImageView = itemView.findViewById(R.id.flagImageView);
            countryNameTextView = itemView.findViewById(R.id.countryNameTextView);
            countryActiveCasesTextView = itemView.findViewById(R.id.countryActiveCasesTextView);
        }
    }
}

package com.example.covid_19tracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.DistrictDataActivity;
import com.example.covid_19tracker.R;
import com.example.covid_19tracker.model.StateDataModelClass;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StateDataAdapter extends RecyclerView.Adapter<StateDataAdapter.StateViewHolder> {

    private Context context;
    private List<StateDataModelClass> stateDataModelClassList;
    private String searchCharacters = "";
    private SpannableStringBuilder spannableStringBuilder;

    public StateDataAdapter(Context context, List<StateDataModelClass> stateDataModelClassList) {
        this.context = context;
        this.stateDataModelClassList = stateDataModelClassList;
    }

    @NonNull
    @Override
    public StateDataAdapter.StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.state_display_item, parent, false);
        return new StateViewHolder(view);
    }

    public void searchStateList(List<StateDataModelClass> searchList, String characters){
        stateDataModelClassList = searchList;
        this.searchCharacters = characters;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StateDataAdapter.StateViewHolder holder, int position) {
        StateDataModelClass stateDataModelClass = stateDataModelClassList.get(position);

        String active = stateDataModelClass.getActive();
        holder.stateActiveCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));

        String confirmed = stateDataModelClass.getConfirmed();

        String deaths = stateDataModelClass.getDeaths();

        String lastupdatedtime = stateDataModelClass.getLastupdatedtime();

        String recovered = stateDataModelClass.getRecovered();


        String state = stateDataModelClass.getState();
        if(searchCharacters.length()>0){
            // adding colours to searched characters
            spannableStringBuilder = new SpannableStringBuilder(state);
            Pattern pattern = Pattern.compile(searchCharacters.toLowerCase());
            Matcher matcher = pattern.matcher(state.toLowerCase());
            while (matcher.find()){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#D1F792"));
                spannableStringBuilder.setSpan(foregroundColorSpan, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.stateNameTextView.setText(spannableStringBuilder);
        }else {
            holder.stateNameTextView.setText((position+1) + ". " + state);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DistrictDataActivity.class);
                intent.putExtra("State Name",state);
                intent.putExtra("Confirmed Cases",confirmed);
                intent.putExtra("Active Cases",active);
                intent.putExtra("Recovered",recovered);
                intent.putExtra("Deaths",deaths);
                intent.putExtra("Last Time Updated",lastupdatedtime);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stateDataModelClassList.size();
    }

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        TextView stateNameTextView, stateActiveCasesTextView;

        public StateViewHolder(@NonNull View itemView) {
            super(itemView);

            stateNameTextView = itemView.findViewById(R.id.stateNameTextView);
            stateActiveCasesTextView = itemView.findViewById(R.id.stateActiveCasesTextView);
        }
    }
}

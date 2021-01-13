package com.example.covid_19tracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.model.StateDataModelClass;

import java.text.NumberFormat;
import java.util.List;

public class StateDataAdapter extends RecyclerView.Adapter<StateDataAdapter.StateViewHolder> {

    private Context context;
    private List<StateDataModelClass> stateDataModelClassList;

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

    public void searchStateList(List<StateDataModelClass> searchList){
        stateDataModelClassList = searchList;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StateDataAdapter.StateViewHolder holder, int position) {
        StateDataModelClass stateDataModelClass = stateDataModelClassList.get(position);

        String state = stateDataModelClass.getState();
        holder.stateNameTextView.setText((position+1) + ". " + state);

        String active = stateDataModelClass.getActive();
        holder.stateActiveCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));

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

package com.example.covid_19tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.model.DistrictDataModelClass;

import java.text.NumberFormat;
import java.util.List;

public class DistrictDataAdapter extends RecyclerView.Adapter<DistrictDataAdapter.DistrictViewHolder> {

    private Context context;
    private List<DistrictDataModelClass> districtDataModelClassList;

    public DistrictDataAdapter(Context context, List<DistrictDataModelClass> districtDataModelClassList) {
        this.context = context;
        this.districtDataModelClassList = districtDataModelClassList;
    }

    @NonNull
    @Override
    public DistrictDataAdapter.DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.district_display_item, parent, false);
        return new DistrictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictDataAdapter.DistrictViewHolder holder, int position) {
        DistrictDataModelClass districtDataModelClass = districtDataModelClassList.get(position);

        String district = districtDataModelClass.getDistrict();
        holder.districtNameTextView.setText(district);

        String active = districtDataModelClass.getActive();
        holder.districtActiveCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(active)));

        String deceased = districtDataModelClass.getDeceased();
        holder.districtDeceasedCasesTextView.setText(NumberFormat.getInstance().format(Integer.parseInt(deceased)));
    }

    @Override
    public int getItemCount() {
        return districtDataModelClassList.size();
    }

    public static class DistrictViewHolder extends RecyclerView.ViewHolder {

        TextView districtActiveCasesTextView, districtDeceasedCasesTextView, districtNameTextView;

        public DistrictViewHolder(@NonNull View itemView) {
            super(itemView);

            districtActiveCasesTextView = itemView.findViewById(R.id.districtActiveCasesTextView);
            districtDeceasedCasesTextView = itemView.findViewById(R.id.districtDeceasedCasesTextView);
            districtNameTextView = itemView.findViewById(R.id.districtNameTextView);
        }
    }
}

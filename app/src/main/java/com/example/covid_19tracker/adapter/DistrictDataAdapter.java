package com.example.covid_19tracker.adapter;

import android.content.Context;
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

import com.example.covid_19tracker.R;
import com.example.covid_19tracker.model.DistrictDataModelClass;
import com.example.covid_19tracker.model.StateDataModelClass;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistrictDataAdapter extends RecyclerView.Adapter<DistrictDataAdapter.DistrictViewHolder> {

    private Context context;
    private List<DistrictDataModelClass> districtDataModelClassList;
    private String searchCharacters = "";
    private SpannableStringBuilder spannableStringBuilder;

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

    public void searchStateList(List<DistrictDataModelClass> searchList, String characters){
        districtDataModelClassList = searchList;
        this.searchCharacters = characters;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictDataAdapter.DistrictViewHolder holder, int position) {
        DistrictDataModelClass districtDataModelClass = districtDataModelClassList.get(position);

        String district = districtDataModelClass.getDistrict();

        if(searchCharacters.length()>0){
            // adding colours to searched characters
            spannableStringBuilder = new SpannableStringBuilder(district);
            Pattern pattern = Pattern.compile(searchCharacters.toLowerCase());
            Matcher matcher = pattern.matcher(district.toLowerCase());
            while (matcher.find()){
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#D1F792"));
                spannableStringBuilder.setSpan(foregroundColorSpan, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            holder.districtNameTextView.setText(spannableStringBuilder);
        }else {
            holder.districtNameTextView.setText(district);
        }

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

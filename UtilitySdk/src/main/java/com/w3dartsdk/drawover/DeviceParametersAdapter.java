package com.w3dartsdk.drawover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w3dartsdk.model.DeviceData;
import com.w3dartsdk.R;

import java.util.ArrayList;

public class DeviceParametersAdapter extends RecyclerView.Adapter<DeviceParametersAdapter.ExerciseHolder> {

    ArrayList<DeviceData> deviceDataArrayList;
    Context context;

    public DeviceParametersAdapter(Context context, ArrayList<DeviceData> list) {
        this.deviceDataArrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parameter, parent, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseHolder holder, final int position) {

        if (deviceDataArrayList.size() > 0) {

            DeviceData deviceData = deviceDataArrayList.get(holder.getAdapterPosition());
            holder.textViewKey.setText(deviceData.getKey());
            holder.textViewValue.setText(deviceData.getValue());

        }

    }

    @Override
    public int getItemCount() {
        return deviceDataArrayList == null ? 0 : deviceDataArrayList.size();
    }

    static class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView textViewKey;
        TextView textViewValue;

        ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            textViewKey = itemView.findViewById(R.id.tv_parameter_key);
            textViewValue = itemView.findViewById(R.id.tv_parameter_value);
        }
    }
}
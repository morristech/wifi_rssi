package com.aruba.wifirssi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aruba.wifirssi.R;
import com.aruba.wifirssi.model.WifiAccessPoint;

import java.util.List;

public class WifiAccessPointListAdapter extends RecyclerView.Adapter<WifiAccessPointListAdapter.WifiAccessPointViewHolder> {

    private static final String TAG = "WifiAccessPointListAdapter";

    private Context context;
    private List<WifiAccessPoint> wifiAccessPoints;

    public WifiAccessPointListAdapter(@NonNull Context context, @NonNull List<WifiAccessPoint> accessPoints) {
        this.context = context;
        this.wifiAccessPoints = accessPoints;
    }

    @NonNull
    @Override
    public WifiAccessPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ap_item, parent, false);
        return new WifiAccessPointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WifiAccessPointViewHolder holder, int position) {
        final WifiAccessPoint person = wifiAccessPoints.get(position);
        holder.name.setText(person.getSsid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2019-11-13 : Create some action here
            }
        });
    }

    @Override
    public int getItemCount() {
        return (wifiAccessPoints != null ? wifiAccessPoints.size() : 0);
    }

    public class WifiAccessPointViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public WifiAccessPointViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

}

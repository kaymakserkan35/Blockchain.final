package com.betelgeuse.blockchain.userinterface.information;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.userinterface.model.InformationModel;

import java.util.ArrayList;
import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationViewHolder> {

    InformationItemClickListener itemClickListener;

    public void setItemClickListener(InformationItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    List<InformationModel> infos;

    public InformationAdapter(List<InformationModel> infos) {
        if (infos != null) this.infos = infos;
        else infos = new ArrayList<>();
    }

    public void setInfos(List<InformationModel> infos) {
        this.infos = infos;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View info = LayoutInflater.from(parent.getContext()).inflate(R.layout.information, parent, false);
        return new InformationViewHolder(info);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        InformationModel infoModel = infos.get(position);
        int infoModelPosition = holder.getLayoutPosition();
        holder.fromCurrency.setText(infoModel.fromCurrency);
        holder.toCurrency.setText(infoModel.toCurrency);
        holder.rsi.setText(String.valueOf(infoModel.rsi));
        holder.bollingBand.setText(infoModel.bolling);
        holder.calendar.setText(infoModel.calendar);

        holder.delete.setOnClickListener(v -> InformationAdapter.this.itemClickListener.itemOnClickListener(v, infos.get(infoModelPosition)));
        holder.setting.setOnClickListener(v -> InformationAdapter.this.itemClickListener.itemOnClickListener(v, infos.get(infoModelPosition)));
        holder.symbolLayout.setOnClickListener(v -> InformationAdapter.this.itemClickListener.itemOnClickListener(v, infos.get(infoModelPosition)));

    }

    @Override
    public int getItemCount() {
        if (infos != null) return infos.size();
        else return 0;

    }
}

package com.betelgeuse.blockchain.userinterface.information;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betelgeuse.blockchain.R;

public class InformationViewHolder extends RecyclerView.ViewHolder {
    TextView fromCurrency, toCurrency, rsi, bollingBand, calendar;
    ImageButton delete, setting;
    LinearLayout symbolLayout;

    public InformationViewHolder(@NonNull View information) {
        super(information);
        fromCurrency = information.findViewById(R.id.fromCurrencies);
        toCurrency = information.findViewById(R.id.toCurrency);
        rsi = information.findViewById(R.id.rsi);
        bollingBand = information.findViewById(R.id.bollingBand);
        calendar = information.findViewById(R.id.calendar);
        delete = information.findViewById(R.id.delete);
        setting = information.findViewById(R.id.setting);
        symbolLayout=information.findViewById(R.id.symbolLayout);



    }
}


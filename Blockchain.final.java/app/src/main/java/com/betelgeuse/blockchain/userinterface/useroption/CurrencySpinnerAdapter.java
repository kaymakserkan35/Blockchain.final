package com.betelgeuse.blockchain.userinterface.useroption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.betelgeuse.blockchain.R;
import com.betelgeuse.blockchain.userinterface.model.CurrencyModel;

import java.util.List;

public class CurrencySpinnerAdapter  extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<CurrencyModel> currencyModels;
    public CurrencySpinnerAdapter(Context context, List<CurrencyModel> currencyModels) {
        this.currencyModels = currencyModels;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public  void  setData(List<CurrencyModel> currencyModels) {
        this.currencyModels = currencyModels;
    }
    @Override
    public int getCount() {
        return currencyModels.size();
    }

    @Override
    public Object getItem(int position) {
      return   currencyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.currency_spinner_item, null);
        TextView currencyName = convertView.findViewById(R.id.currencyName);
        currencyName.setText(currencyModels.get(position).code);
       return  convertView;
    }
}

package com.betelgeuse.blockchain.userinterface.model.modelListener;

import com.betelgeuse.blockchain.userinterface.model.CurrencyModel;

import java.util.List;

public interface CurrencyModelListListener {
    public  void onSuccess(List<CurrencyModel> currencyModels);
}

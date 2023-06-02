package com.betelgeuse.blockchain.data;

import com.betelgeuse.blockchain.data.dataListener.CurrencyDTOListListener;

public interface ICurrencyOperations {
    public  void  readCurrencies(CurrencyDTOListListener currencies);
}

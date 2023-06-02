package com.betelgeuse.blockchain.userinterface.model;

import com.betelgeuse.blockchain.data.dto.CurrencyDTO;

import java.util.ArrayList;
import java.util.List;

public class CurrencyModel {

    public CurrencyModel(CurrencyDTO currencyDTO) {
        this.code = currencyDTO.getCode();
        this.country = currencyDTO.getCountry();
        this.currency = currencyDTO.getCurrency();
        this.symbol = currencyDTO.getSymbol();
    }

    public String code;
    public String country;
    public String currency;
    public String symbol;

    public static List<CurrencyModel> convertCurrencyDTOListToCurrencyModelList(List<CurrencyDTO> currencyDTOList) {
        List<CurrencyModel> currencyModels = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOList
        ) {
            currencyModels.add(new CurrencyModel(currencyDTO));
        }
        return currencyModels;
    }
}

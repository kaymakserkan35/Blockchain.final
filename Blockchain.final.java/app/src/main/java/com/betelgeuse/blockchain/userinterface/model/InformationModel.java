package com.betelgeuse.blockchain.userinterface.model;

import com.betelgeuse.blockchain.core.indicator.Data;
import com.betelgeuse.blockchain.core.indicator.Signal;

import java.util.ArrayList;
import java.util.List;

public class InformationModel {
    //do not hold data

    public String calendar;
    public String symbol;
    public String fromCurrency;
    public String toCurrency;
    public String rsi;
    public String bolling;

    Signal signal;

    public InformationModel(Data data) {
        this.fromCurrency = data.symbol.split("/")[0];
        this.toCurrency = data.symbol.split("/")[1];
        this.calendar = data.calendar;
        this.symbol = data.symbol;
        this.signal = new Signal(data);
        this.rsi = signal.rsiALSat(data.getRsiData().relativeStrengthIndex);
        this.bolling = signal.bollingAlSat(
                data.getBollingerBandData().averagePrice,
                data.getBollingerBandData().lowerBand,
                data.getBollingerBandData().upperBand,
                data.getBollingerBandData().middleBand);
    }

    public static List<InformationModel> convertDataListToInfoModelList(List<Data> dataList) {
        List<InformationModel> informationModelList = new ArrayList<>();
        for (Data data : dataList) {
            InformationModel informationModel = new InformationModel(data);
            informationModelList.add(informationModel);
        }
        return informationModelList;
    }

}

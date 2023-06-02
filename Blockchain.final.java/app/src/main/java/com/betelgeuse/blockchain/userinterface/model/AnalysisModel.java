package com.betelgeuse.blockchain.userinterface.model;

import com.betelgeuse.blockchain.core.indicator.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalysisModel {
    public String fromCurrency;
    public String toCurrency;
    public String calendar;
    public String high;
    public String low;
    public String price;
    public String symbol;
    public String open;


    public String rsi;
    public String lowerBand;
    public String upperBand;
    public String middleBand;

    public AnalysisModel(Data data) {
        this.symbol = data.fromCurrency + "/" + data.toCurrency;
        this.price =  decimalFormat( data.price );
        this.fromCurrency = data.fromCurrency;
        this.toCurrency = data.toCurrency;
        this.calendar = data.calendar;
        this.rsi = decimalFormat( data.getRsiData().relativeStrengthIndex );
        this.middleBand =  decimalFormat( data.getBollingerBandData().middleBand );
        this.lowerBand =  decimalFormat( data.getBollingerBandData().lowerBand );
        this.upperBand =  decimalFormat( data.getBollingerBandData().upperBand );
    }

    private String decimalFormat(double doubleValue){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(doubleValue);
    }

    public static List<AnalysisModel> convertDataListToAnalysisModel(List<Data> dataList) {
        List<AnalysisModel> models = new ArrayList<>();
        for (Data data : dataList
        ) {
            AnalysisModel model = new AnalysisModel(data);
            models.add(model);
        }
        return models;
    }
}

package com.betelgeuse.blockchain.core.indicator;

import com.betelgeuse.blockchain.data.dto.TickerDTO;

import java.util.ArrayList;
import java.util.List;

public class Data extends AData {
    protected RsiData           rsiData;
     public RsiData getRsiData ( ) {
        return rsiData;
    }
    public BollingerBandData getBollingerBandData ( ) {
        return bollingerBandData;
    }
     BollingerBandData bollingerBandData;
    protected Data initRsiData () {
        this.rsiData = new RsiData(this);
        return  this;
    }
    protected Data initBollingerBandData () {
        this.bollingerBandData = new BollingerBandData();
        return  this;
    }
    public Data (TickerDTO tickerDTO) {
        super(tickerDTO);
        initRsiData().initBollingerBandData();
    }
    public class BollingerBandData {
        public   double averagePrice;
        public double  middleBand;
        public  double lowerBand;
        public  double upperBand;
    }
    public class RsiData {

        RsiData (Data data) {
            double change = data.price - data.open;
            if (change > 0) {
                this.changeUpward = Math.abs(change);
                this.changeDownward = 0;
            }
            if (change < 0) {
                this.changeDownward = Math.abs(change);
                this.changeUpward = 0;
            }
            if (change == 0) {
                this.changeUpward = 0;
                this.changeDownward = 0;
            }
        }

        double changeUpward;
        double changeDownward;
        double averageUpward;
        double averageDownward;
        double averagePrice;
        public double relativeStrength;
        public double relativeStrengthIndex;
    }
    public static List<Data> convertTickerListToDataList (List<TickerDTO> tickers) {

        List<Data> dataList = new ArrayList<>();
        for (TickerDTO ticker : tickers) {
            Data data = new Data(ticker).initRsiData().initBollingerBandData();
            dataList.add(data);
        }

        return dataList;
    }
}

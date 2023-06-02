package com.betelgeuse.blockchain.core.indicator;

import com.betelgeuse.blockchain.H;
import com.betelgeuse.blockchain.core.indicator.Data;
import com.betelgeuse.blockchain.core.indicator.Indicator;
import com.betelgeuse.blockchain.core.indicator.Period;

import java.util.List;
import java.util.Locale;

public class BollingerBand extends Indicator {
    public BollingerBand (List<Data> dataList, Period period) {
        super(dataList, period);
    }
    public BollingerBand(List<Data> dataList){
        //default olarak period 20 alınır!!
        super(dataList,Period.twentyDay);
    }

    @Override
    public Indicator analyze ( ) {

        average.analyze();
        sortDataListByCalendar(Sort.DESCENDING);

        for (int i = 0; i < dataList.size()- period.getCode(); i++) {
            if ((dataList.size() - (i)) < period.getCode()) return  null;
            List<Data> _dataList = null;
            try {
                _dataList = dataList.subList(i, period.getCode() + i);
            } catch (Exception e) {
                H.errorLog(this.getClass().getSimpleName(), "analyze", e.getLocalizedMessage().toUpperCase(Locale.ROOT));
            }
            calculateBands(_dataList);
        }

        return  this;
    }



    private void calculateBands(List<Data> dataList){
        if (dataList.size() < period.getCode()) {
            return;
        }
        double sum = 0;
        for (int i = 0; i < period.getCode(); i++) {
            double average = dataList.get(0).bollingerBandData.averagePrice;
            Data data = dataList.get(i);
            double sapma = data.price - average;
            double  squareOfDeviation = Math.pow( sapma,2);
            // if (sapma<0) {squareOfDeviation = squareOfDeviation * (-1);} dogru degil
            sum = sum + squareOfDeviation;
        }


        double variance = sum/(period.getCode()-1);
        double standardDeviation = Math.sqrt(variance);
        double middleBand = dataList.get(0).bollingerBandData.averagePrice;
        double upperBand = dataList.get(0).bollingerBandData.averagePrice + (2*standardDeviation);
        double lowerBand = dataList.get(0).bollingerBandData.averagePrice - (2*standardDeviation);

        dataList.get(0).bollingerBandData.middleBand = middleBand;
        dataList.get(0).bollingerBandData.lowerBand = lowerBand;
        dataList.get(0).bollingerBandData.upperBand = upperBand;

    }

}

package com.betelgeuse.blockchain.core.indicator;

import com.betelgeuse.blockchain.H;

import java.util.List;
import java.util.Locale;

public class SimpleMovingAverage extends Average {
    public SimpleMovingAverage(List<Data> dataList, Period period) {
        super(dataList, period);
    }

    @Override
    public Average analyze() {
        if (validate(dataList, period)) return this;
        sortDataByCalendar(Sort.DESCENDING);

        for (int i = 0; i < dataList.size() - period.getCode(); i++) {
            List<Data> _dataList = null;
            try {
                _dataList = dataList.subList(i, period.getCode() + i);
            } catch (Exception e) {
                H.errorLog(this.getClass().getSimpleName(), "analyze", e.getLocalizedMessage().toUpperCase(Locale.ROOT));
            }
            averageFirstDataInArray(_dataList, this.period);
        }
        return this;
    }

    private void averageFirstDataInArray(List<Data> dataList, Period period) {
        if (dataList.size() < period.getCode()) return;
        double sumPrice = 0, averagePrice;
        double sumDownward = 0, averageDownward;
        double sumUpward = 0, averageUpward;

        for (int i = 0; i < period.getCode(); i++) {
            sumPrice = sumPrice + dataList.get(i).price;
            sumDownward = sumDownward + dataList.get(i).rsiData.changeDownward;
            sumUpward = sumUpward + dataList.get(i).rsiData.changeUpward;
        }
        averagePrice = sumPrice / period.getCode();
        averageDownward = sumDownward / period.getCode();
        averageUpward = sumUpward / period.getCode();

        dataList.get(0).rsiData.averagePrice = averagePrice;
        dataList.get(0).rsiData.averageUpward = averageUpward;
        dataList.get(0).rsiData.averageDownward = averageDownward;
    }
}

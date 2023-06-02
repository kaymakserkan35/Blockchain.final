package com.betelgeuse.blockchain.core.indicator;

import java.util.Iterator;
import java.util.List;

public class RelativeStrengthIndex extends Indicator {
    public RelativeStrengthIndex (List<Data> dataList, Period period) {
        super(dataList, period);
    }

    @Override
    public Indicator analyze ( ) {
        sortDataListByCalendar(Sort.DESCENDING);
        average.analyze();
        Iterator<Data> dataIterator = dataList.iterator();
        while (dataIterator.hasNext()) {
            Data dataCurrent = dataIterator.next();
            dataCurrent.rsiData.relativeStrength = dataCurrent.rsiData.averageUpward / dataCurrent.rsiData.averageDownward;
            dataCurrent.rsiData.relativeStrengthIndex = 100 - (100 * (1 / (1 + (dataCurrent.rsiData.relativeStrength))));
        }
        return this;
    }
}

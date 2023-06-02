package com.betelgeuse.blockchain.core.indicator;

import java.util.List;

public class MovingAverageConvergenceDivergence extends Indicator {
    protected MovingAverageConvergenceDivergence (List<Data> dataList, Period period) {
        super(dataList, period);
    }

    @Override
    public Indicator analyze ( ) {
        return  this;
    }
}

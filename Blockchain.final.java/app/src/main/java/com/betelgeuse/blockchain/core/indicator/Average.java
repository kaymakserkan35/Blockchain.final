package com.betelgeuse.blockchain.core.indicator;

import android.os.Build;
import androidx.annotation.Nullable;
import com.betelgeuse.blockchain.H;
import java.util.Comparator;
import java.util.List;

public abstract class Average {
     protected List<Data> dataList;
     protected Period     period;
    public Average (List<Data> dataList,@Nullable Period period) {
        if (period!= null) this.period = period;
        else  this.period = Period.fourteenDay;
        this.dataList = dataList;

    }
    public Average setData (List<Data> dataList,@Nullable Period period) {
        if (period!= null) this.period = period;
        else  this.period = Period.fourteenDay;
        this.dataList = dataList;
        return this;
    }
    public Average sortDataByCalendar (Sort sort) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dataList.sort(new Comparator<Data>() {
                @Override
                public int compare (Data o1, Data o2) {
                    if (sort.equals(Sort.ASCENDING)) return o1.calendar.compareTo(o2.calendar);
                    else return o2.calendar.compareTo(o1.calendar);
                }
            });

        } else H.errorLog(this.getClass().getSimpleName(), "sortDataByDate", "(Build.VERSION.SDK_INT eterli degil!");

        return  this;
    }
    public abstract Average analyze ( );
    public boolean validate(List<Data> dataList, Period period) {
        return (dataList.size() > period.getCode()) ? true : false;
    }
    public double findMaxAbs(double... args) {
        double result = 0;
        for (double val : args
        ) {
            val = Math.abs(val);
            if (val > result) result = val;
        }
        return result;
    }
}

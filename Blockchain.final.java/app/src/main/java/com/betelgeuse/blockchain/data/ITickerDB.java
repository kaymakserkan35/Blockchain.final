package com.betelgeuse.blockchain.data;

import androidx.annotation.Nullable;

import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListener;

public interface ITickerDB {
    void readTickerOfDateLIVE(String date, String toCurrency, TickerDTOListener listener);
    void readTickersOfDate(String date,  TickerDTOListListener listener);
    void readTickerFromDateToNow(String toCurrency, String fromDate,  TickerDTOListListener listener);
    void readTickerOfDate(String toCurrency, String dateValue,  TickerDTOListener listener);
}

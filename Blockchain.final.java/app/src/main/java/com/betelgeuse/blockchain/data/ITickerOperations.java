package com.betelgeuse.blockchain.data;

import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;

public interface ITickerOperations {
    void getTickerHistory(String toCurrency, int history, TickerDTOListListener listener);
    void  getTickerHistoryLIVE(String toCurrency, int history,TickerDTOListListener listener);
}

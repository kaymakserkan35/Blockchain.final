package com.betelgeuse.blockchain.data.firebase;

import android.os.Build;
import android.util.Log;

import com.betelgeuse.blockchain.data.ADatabase;
import com.betelgeuse.blockchain.data.ITickerOperations;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.stream.Collectors;

public class TickerDB extends ADatabase implements ITickerOperations {

    TickerDBSERVER tickerDBSERVER = new TickerDBSERVER(FirebaseFirestore.getInstance());
    TickerDBCache tickerDBCache = new TickerDBCache(FirebaseFirestore.getInstance());


    private void readTickerFromDateToNow(String toCurrency, String fromCalendar, TickerDTOListListener listener) {
        tickerDBCache.readTickerFromDateToNow(toCurrency, fromCalendar, tickerList -> {
            listener.onSuccess(tickerList);
            if (tickerList.size() == 0) {
                tickerDBSERVER.readTickerFromDateToNow(toCurrency, fromCalendar, listener);
            }
        });

    }

    @Override
    public void getTickerHistory(String toCurrency, int history, TickerDTOListListener listener) {
        String fromDate = getDaysAgoUTC_AsSimpleDateFormatString(history);
        /*------------------------------------------------------------------*/
        readTickerFromDateToNow(toCurrency, fromDate, listener);

    }

    @Override
    public void getTickerHistoryLIVE(String toCurrency, int history, TickerDTOListListener listener) {
        String fromDate = getDaysAgoUTC_AsSimpleDateFormatString(history);
        /*------------------------------------------------------------------*/
        readTickerFromDateToNow(toCurrency, fromDate, tickerList -> {
            tickerDBSERVER.readTickerOfDateLIVE(getDate_AsSimpleDateFormatString(), toCurrency, ticker -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    List<TickerDTO> returningTickerList = tickerList.stream().filter(tick -> !(tick.getCalendar().equalsIgnoreCase(getDate_AsSimpleDateFormatString()))).collect(Collectors.toList());

                    returningTickerList.add(ticker);
                    listener.onSuccess(returningTickerList);
                    /*------------------LOG----------------------*/
                    for (TickerDTO t : returningTickerList
                    ) {
                        Log.d("getTickerHistoryLIVE", t.getCalendar());
                    }
                    /*------------------------------------------------------*/
                }
            });
        });


    }

}

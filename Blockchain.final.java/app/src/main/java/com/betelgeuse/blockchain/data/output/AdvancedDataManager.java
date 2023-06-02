package com.betelgeuse.blockchain.data.output;

import com.betelgeuse.blockchain.data.ADatabase;
import com.betelgeuse.blockchain.data.dto.TickerDTO;

import java.util.List;

public class AdvancedDataManager extends ADatabase {

    protected TickerDTO converter(String fromCurrency, String toCurrency, TickerDTO tickerA, TickerDTO tickerB) {
        if (!tickerA.getCalendar().equalsIgnoreCase(tickerB.getCalendar())) return null;
        if (tickerB.getToCurrency().equalsIgnoreCase(fromCurrency) && tickerA.getToCurrency().equalsIgnoreCase(toCurrency)) {
            TickerDTO returningTicker = new TickerDTO();
            returningTicker.setSymbol(fromCurrency + "/" + toCurrency);
            returningTicker.setCalendar(tickerA.getCalendar());
            returningTicker.setPrice(tickerA.getPrice() / tickerB.getPrice());
            returningTicker.setLow(tickerA.getLow() / tickerB.getLow());
            returningTicker.setHigh(tickerA.getHigh() / tickerB.getHigh());
            returningTicker.setOpen(tickerA.getOpen() / tickerB.getOpen());
            returningTicker.setCandle(returningTicker.getPrice() - returningTicker.getOpen());
            return  returningTicker;
        }
        if (tickerA.getToCurrency().equalsIgnoreCase(fromCurrency) && tickerB.getToCurrency().equalsIgnoreCase(toCurrency)) {
            TickerDTO returningTicker = new TickerDTO();
            returningTicker.setSymbol(fromCurrency + "/" + toCurrency);
            returningTicker.setCalendar(tickerB.getCalendar());
            returningTicker.setPrice(tickerB.getPrice() / tickerA.getPrice());
            returningTicker.setLow(tickerB.getLow() / tickerA.getLow());
            returningTicker.setHigh(tickerB.getHigh() / tickerA.getHigh());
            returningTicker.setOpen(tickerB.getOpen() / tickerA.getOpen());
            returningTicker.setCandle(returningTicker.getPrice() - returningTicker.getOpen());
            return  returningTicker;
        }
        return null;
    }

    protected void reverseTicker(TickerDTO ticker) {
        ticker.setCalendar(ticker.getCalendar());
        ticker.setTimeStamp(ticker.getTimeStamp());
        ticker.setSymbol(ticker.getToCurrency() + "/" + ticker.getFromCurrency());
        ticker.setPrice(1 / ticker.getPrice());
        ticker.setOpen(1 / ticker.getOpen());
        ticker.setHigh(1 / ticker.getHigh());
        ticker.setLow(1 / ticker.getLow());
        ticker.setTimeStamp(ticker.getTimeStamp());
        ticker.setCandle(ticker.getPrice() - ticker.getOpen());
    }

    protected void reverseTickers(List<TickerDTO> tickers) {
        for (TickerDTO ticker : tickers) {
            reverseTicker(ticker);
        }
    }

}

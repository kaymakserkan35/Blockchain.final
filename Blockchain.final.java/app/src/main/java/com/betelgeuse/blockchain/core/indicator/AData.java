package com.betelgeuse.blockchain.core.indicator;

import com.betelgeuse.blockchain.data.dto.TickerDTO;

 abstract class AData {
    public AData (TickerDTO tickerDTO) {
        this.fromCurrency = tickerDTO.getFromCurrency();
        this.toCurrency = tickerDTO.getToCurrency();
        this.calendar = tickerDTO.getCalendar();
        this.symbol = tickerDTO.getSymbol();
        this.price = tickerDTO.getPrice();
        this.open = tickerDTO.getOpen();
        this.high = tickerDTO.getHigh();
        this.low = tickerDTO.getLow();
        this.candle = tickerDTO.getCandle();
        this.timeStamp = tickerDTO.getTimeStamp();
    }

    public String fromCurrency;
    public String toCurrency;
    double candle;
    public String calendar;
    double high;
    double low;
    public double price;
    public String symbol;
    double timeStamp;
    double open;
}

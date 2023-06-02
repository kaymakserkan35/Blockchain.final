package com.betelgeuse.blockchain.userinterface.model;

import com.betelgeuse.blockchain.data.dto.TickerDTO;

import java.util.ArrayList;
import java.util.List;

public class TickerModel {


    public TickerModel(TickerDTO tickerDTO) {
        if (tickerDTO==null) return;
        converter(tickerDTO);
    }

    public TickerModel converter(TickerDTO tickerDTO) {
        if (tickerDTO==null) return null;
        this.symbol = tickerDTO.getSymbol();
        setCurrencyNames();
        this.price = tickerDTO.getPrice();
        this.candle = tickerDTO.getCandle();
        this.calendar = tickerDTO.getCalendar();
        this.timeStamp = tickerDTO.getTimeStamp();
        this.open = tickerDTO.getOpen();
        this.low = tickerDTO.getLow();
        return  this;
    }

    public List<TickerModel> convertTickerDtoTOTickerModel(List<TickerDTO> tickerDTOList) {
        List<TickerModel> tickerModelList = new ArrayList<>();
        for (TickerDTO tickerDTO: tickerDTOList
             ) {
            tickerModelList.add(new TickerModel(tickerDTO));
        }
        return  tickerModelList;
    }

    double candle;
    String calendar;
    double high;
    double low;
    double price;
    String symbol;
    double timeStamp;
    double open;
    String toCurrency;
    String fromCurrency;

    public String getToCurrency ( ) {
        return toCurrency;
    }
    public String getFromCurrency ( ) {
        return fromCurrency;
    }
    public void setCurrencyNames(){
        String[] arr = symbol.split("/");
        this.fromCurrency = arr[0];
        this.toCurrency = arr[1];
    }


    /*------------------------------------------------*/

    public double getCandle ( ) {
        return candle;
    }

    public void setCandle (double candle) {
        this.candle = candle;
    }

    public String getCalendar ( ) {
        return calendar;
    }

    public void setCalendar (String calendar) {
        this.calendar = calendar;
    }

    public double getHigh ( ) {
        return high;
    }

    public void setHigh (double high) {
        this.high = high;
    }

    public double getLow ( ) {
        return low;
    }

    public void setLow (double low) {
        this.low = low;
    }

    public double getPrice ( ) {
        return price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public String getSymbol ( ) {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
        setCurrencyNames();
    }

    public double getTimeStamp ( ) {
        return timeStamp;
    }

    public void setTimeStamp (double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getOpen ( ) {
        return open;
    }

    public void setOpen (double open) {
        this.open = open;
    }


}

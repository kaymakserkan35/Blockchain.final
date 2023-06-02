package com.betelgeuse.blockchain.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TickerDTO {

    @SerializedName("candle")  @Expose
    double candle;
    @SerializedName("calendar")  @Expose
    String calendar;
    @SerializedName("high")  @Expose
    double high;
    @SerializedName("low")  @Expose
    double low;
    @SerializedName("price")  @Expose
    double price;
    @SerializedName("symbol")  @Expose
    String symbol;
    @SerializedName("timeStamp")  @Expose
    double timeStamp;
    @SerializedName("open")  @Expose
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

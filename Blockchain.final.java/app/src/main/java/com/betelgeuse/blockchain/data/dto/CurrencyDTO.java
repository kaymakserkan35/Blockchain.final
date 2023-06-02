package com.betelgeuse.blockchain.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyDTO {


    public CurrencyDTO(String deneme) {

    }
    public CurrencyDTO(){}

    @SerializedName("code")  @Expose
    String code;
    @SerializedName("country")  @Expose
    String country;
    @SerializedName("currency")  @Expose
    String currency;
    @SerializedName("symbol")  @Expose
    String symbol;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}

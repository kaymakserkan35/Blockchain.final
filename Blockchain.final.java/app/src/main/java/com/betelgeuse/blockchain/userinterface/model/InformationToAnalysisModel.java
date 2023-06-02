package com.betelgeuse.blockchain.userinterface.model;

public class InformationToAnalysisModel {
    public String fromCurrency;
    public String toCurrency;
    public String email;
    public InformationToAnalysisModel( String email,String fromCurrency, String toCurrency) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.email = email;
    }


}

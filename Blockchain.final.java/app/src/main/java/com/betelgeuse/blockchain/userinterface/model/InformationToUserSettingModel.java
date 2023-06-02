package com.betelgeuse.blockchain.userinterface.model;

public class InformationToUserSettingModel {
  public   String email;
    public  String fromCurrency;
    public  String toCurrency;

    public InformationToUserSettingModel(String userEmail, String fromCurrency, String toCurrency) {
      this.email = userEmail;
      this.fromCurrency = fromCurrency;
      this.toCurrency = toCurrency;
    }
}

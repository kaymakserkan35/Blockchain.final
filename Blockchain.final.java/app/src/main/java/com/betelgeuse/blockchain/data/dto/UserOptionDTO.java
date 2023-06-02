package com.betelgeuse.blockchain.data.dto;

import androidx.annotation.Nullable;

import com.betelgeuse.blockchain.core.indicator.Period;
import com.betelgeuse.blockchain.userinterface.model.UserOptionModel;

public class UserOptionDTO {
    public  String id;
    public String email;
    public        String fromCurrency;
    public        String toCurrency;
    @Nullable
    public        Period period;
    @Nullable
    public        int    history;
    public  String symbol;
    public boolean vibration;
    public boolean  notification;
    public UserOptionDTO(UserOptionModel userOptionModel) {
        this.email = userOptionModel.email;
        this.fromCurrency = userOptionModel.fromCurrency;
        this.toCurrency = userOptionModel.toCurrency;
        this.period =userOptionModel.period;
        this.history = userOptionModel.history;
        this.symbol = userOptionModel.fromCurrency+"/"+userOptionModel.toCurrency;
        this.vibration = userOptionModel.vibration;
        this.notification = userOptionModel.notification;
    }
    public UserOptionDTO (String email, String fromCurrency, String toCurrency, @Nullable Period period, int history,boolean vibration,boolean notification) {
        this.email = email;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.period = period;
        this.history = history;
        this.notification = notification;
        this.vibration = vibration;
    }

    /*---------------------------------------*/
    public static  String Id_String = "id";
    public static  String Symbol_String = "symbol";
    public static String Email_String="email";
    public static String FromCurrency_String ="fromCurrency";
    public static String ToCurrency_String="toCurrency";
    @Nullable
    public static String Period_INT="period";
    @Nullable
    public static  String History_INT="history";
    public static String Vibration_INT = "vibration";
    public static String  Notification_INT = "notification";
}

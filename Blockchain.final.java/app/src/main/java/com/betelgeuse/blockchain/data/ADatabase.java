package com.betelgeuse.blockchain.data;
import android.icu.util.Currency;
import android.os.Build;
import com.betelgeuse.blockchain.core.libs.DateWrapper;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public abstract class ADatabase {
    protected    String baseCurrency = "USD";
    private DateWrapper dateWrapper;
    private String timezoneOfLondra   = "GMT";
    private String dateFormatPattern1  = "yyy-MM-dd";
    private String dateFormatPattern2 = "yyyy-MM-dd HH:mm:ss";

    public ADatabase () {
        dateWrapper = new DateWrapper();
    }
    public String generateUniqueID ( ) {
        String uniqueID = getDate_AsSimpleDateFormatString() + "_" + UUID.randomUUID().toString();
        return uniqueID;
    }

    public String getDaysAgoUTC_AsSimpleDateFormatString(int daysAgo){
        return dateWrapper.getDaysAgoUTC_AsSimpleDateFormatString(daysAgo);
    }
    public String getDate_AsSimpleDateFormatString () {
        return   dateWrapper.getDate_AsSimpleDateFormatString();
        // output : 18-03-2022
    }
    public String getYearNowOfCurrentTimeZone ( ) {
        return   dateWrapper.getYearUTC();
    }
    public Date parseStringDateToDateObject(String date) {
        return dateWrapper.convertSimpleDateFormatStringToDateObject(date);
    }
    /*--------------------------------------------------------------------*/
    public Currency getCurrency (String currencyName) {
        Currency currency = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currency = Currency.getInstance(currencyName);
        }
        return currency;

    }
    public String getCurrencyDisplayName (Currency currency) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return   currency.getDisplayName();
        }
        else return  null;
    }
    public Currency getCurrency (Locale locale) {
        Currency currency = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currency = Currency.getInstance(locale);

        }
        return currency;

    }
    public String getCurrencySymbol (Locale locale) {
        String symbol = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            symbol = getCurrency(locale).getSymbol();
        }
        return symbol;
    }
    public Set<Currency> getAllCurrencies ( ) {
        Set<Currency> toret = new HashSet<Currency>();
        Locale[] locs = Locale.getAvailableLocales();
        for (Locale loc : locs) {
            try {
                Currency currency = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    currency = Currency.getInstance(loc);
                }
                if (currency != null) {
                    toret.add(currency);
                }
            } catch (Exception exc) {
                // Locale not found
            }
        }
        return toret;

    }

}

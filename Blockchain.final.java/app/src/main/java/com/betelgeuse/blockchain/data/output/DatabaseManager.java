package com.betelgeuse.blockchain.data.output;

import com.betelgeuse.blockchain.data.ICurrencyOperations;
import com.betelgeuse.blockchain.data.ITickerOperations;
import com.betelgeuse.blockchain.data.IUserOptionOperations;
import com.betelgeuse.blockchain.data.dataListener.CurrencyDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListener;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;
import com.betelgeuse.blockchain.data.firebase.TickerDB;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class DatabaseManager extends AdvancedDataManager {

    ITickerOperations tickerOperations;
    IUserOptionOperations userOptionOperations;
    ICurrencyOperations currencyOperations;

    public DatabaseManager(ITickerOperations tickerOperations, IUserOptionOperations userOptionOperations, ICurrencyOperations currencyOperations) {
        if (currencyOperations != null) this.currencyOperations = currencyOperations;
        if (userOptionOperations != null) this.userOptionOperations = userOptionOperations;
        if (tickerOperations != null) this.tickerOperations = tickerOperations;


    }

    public void getTickerHistory(String fromCurrency, String toCurrency, int history, TickerDTOListListener listener) {
        if (fromCurrency.equalsIgnoreCase(this.baseCurrency)) {
            tickerOperations.getTickerHistory(toCurrency, history, listener);
            return;
        }
        if (toCurrency.equalsIgnoreCase(this.baseCurrency)) {
            tickerOperations.getTickerHistory(fromCurrency, history, tickerList -> {
                reverseTickers(tickerList);
                listener.onSuccess(tickerList);
            });
            return;
        }
        if (!fromCurrency.equalsIgnoreCase(this.baseCurrency) && !toCurrency.equalsIgnoreCase(this.baseCurrency)) {
            if (true) return;

            List<TickerDTO> tickers = new ArrayList<>();
            tickerOperations.getTickerHistory(fromCurrency, history, tickerListA -> {
                tickerOperations.getTickerHistory(toCurrency, history, tickerListB -> {

                    for (int i = 0; i < tickerListA.size(); i++) {
                        TickerDTO tickerDTO = converter(fromCurrency, toCurrency, tickerListA.get(i), tickerListB.get(i));
                        tickers.add(tickerDTO);
                    }

                    listener.onSuccess(tickers);
                });

            });
            return;
        }
    }

    public void getTickerHistoryLIVE(String fromCurrency, String toCurrency, int history, TickerDTOListListener listener) {
        if (fromCurrency.equalsIgnoreCase(this.baseCurrency)) {
            tickerOperations.getTickerHistoryLIVE(toCurrency, history, listener);
            return;
        }
        if (toCurrency.equalsIgnoreCase(this.baseCurrency)) {
            tickerOperations.getTickerHistoryLIVE(fromCurrency, history, tickerList -> {
                reverseTickers(tickerList);
                listener.onSuccess(tickerList);
            });
            return;
        }
        if (!fromCurrency.equalsIgnoreCase(this.baseCurrency) && !toCurrency.equalsIgnoreCase(this.baseCurrency)) {
            if (true) return;

            List<TickerDTO> tickers = new ArrayList<>();
            tickerOperations.getTickerHistoryLIVE(fromCurrency, history, tickerListA -> {
                tickerOperations.getTickerHistoryLIVE(toCurrency, history, tickerListB -> {
                    for (int i = 0; i < tickerListA.size(); i++) {
                        TickerDTO tickerDTO = converter(fromCurrency, toCurrency, tickerListA.get(i), tickerListB.get(i));
                        tickers.add(tickerDTO);
                    }
                    listener.onSuccess(tickers);
                });
            });
            return;
        }

    }

    public void readCurrencies(CurrencyDTOListListener listener) {
        currencyOperations.readCurrencies(listener);
    }

    public void createOrUpdateUserOption(UserOptionDTO userOptionDTO) {
        userOptionOperations.createOrUpdateUserOption(userOptionDTO);
    }

    public void deleteUserOption(String email, String fromCurrency, String toCurrency) {
        userOptionOperations.deleteUserOption(email, fromCurrency, toCurrency);
    }

    public void readUserOptionByEmail(String email, @Nullable UserOptionDTOListListener listener) {
        userOptionOperations.readUserOptionsAllByEmail(email, listener);
    }

    public void readUserOption(String email, String fromCurrency, String toCurrency, UserOptionDTOListener listener) {
        userOptionOperations.readUserOption(email, fromCurrency, toCurrency, listener);
    }
}


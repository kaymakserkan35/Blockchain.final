package com.betelgeuse.blockchain.userinterface.useroption;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.betelgeuse.blockchain.data.dataListener.CurrencyDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListener;
import com.betelgeuse.blockchain.data.dto.CurrencyDTO;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;
import com.betelgeuse.blockchain.data.firebase.CurrencyDB;
import com.betelgeuse.blockchain.data.output.DatabaseManager;
import com.betelgeuse.blockchain.data.sqLite.UserOptionDB;
import com.betelgeuse.blockchain.userinterface.model.CurrencyModel;
import com.betelgeuse.blockchain.userinterface.model.UserOptionModel;
import com.betelgeuse.blockchain.userinterface.model.modelListener.CurrencyModelListListener;

import java.util.List;

public class UserOptionController extends ViewModel {
 private    DatabaseManager databaseManager;

    public UserOptionController(Context context) {
        this.databaseManager = new DatabaseManager(null,new UserOptionDB(context),new CurrencyDB());
    }
    public void readCurrencies(CurrencyModelListListener listener) {
        databaseManager.readCurrencies(new CurrencyDTOListListener() {
            @Override
            public void onSuccess(List<CurrencyDTO> currencies) {
                listener.onSuccess(
                        CurrencyModel.convertCurrencyDTOListToCurrencyModelList(currencies)
                );
            }
        });
    }
    public void createUserOption(UserOptionModel userOptionModel){
        databaseManager.createOrUpdateUserOption(new UserOptionDTO(userOptionModel));
    }
    public  void  readUserOption(String email, String fromCurrency, String toCurrency, UserOptionDTOListener listener) {
        databaseManager.readUserOption(email,fromCurrency,toCurrency,listener);
    }
}

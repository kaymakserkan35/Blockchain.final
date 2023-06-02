package com.betelgeuse.blockchain.userinterface.information;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.betelgeuse.blockchain.H;
import com.betelgeuse.blockchain.core.indicator.BollingerBand;
import com.betelgeuse.blockchain.core.indicator.Data;
import com.betelgeuse.blockchain.core.indicator.Indicator;
import com.betelgeuse.blockchain.core.indicator.RelativeStrengthIndex;
import com.betelgeuse.blockchain.core.indicator.Sort;
import com.betelgeuse.blockchain.core.libs.DateWrapper;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListListener;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;
import com.betelgeuse.blockchain.data.firebase.CurrencyDB;
import com.betelgeuse.blockchain.data.firebase.TickerDB;
import com.betelgeuse.blockchain.data.output.DatabaseManager;
import com.betelgeuse.blockchain.data.sqLite.UserOptionDB;
import com.betelgeuse.blockchain.userinterface.model.InformationModel;
import com.betelgeuse.blockchain.userinterface.model.modelListener.InformationModelListListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InformationController extends ViewModel {
    private   DatabaseManager databaseManager;
    MutableLiveData<List<InformationModel>> infos = new MutableLiveData<>();



    public InformationController getData(String email, Context context) {
        databaseManager = new DatabaseManager(new TickerDB(), new UserOptionDB(context), null);
        databaseManager.readUserOptionByEmail(email, userOptions -> {

            List<InformationModel> _infos = new ArrayList<>();
            for (UserOptionDTO userOption : userOptions
            ) {
                databaseManager.getTickerHistoryLIVE(userOption.fromCurrency, userOption.toCurrency, userOption.history, tickerList -> {
                    List<Data> dataList = Data.convertTickerListToDataList(tickerList);
                    Indicator rsiIndicator = new RelativeStrengthIndex(dataList, userOption.period).analyze();
                    Indicator bollingerBand = new BollingerBand(dataList).analyze().sortDataListByCalendar(Sort.DESCENDING);
                    //  infos.setValue(InformationModel.convertDataListToInfoModelList(dataList)); it works here
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        try {
                            Data returningData = dataList.stream().filter(data -> data.calendar.equalsIgnoreCase(new DateWrapper().getDate_AsSimpleDateFormatString())).findAny().get();
                            _infos.add(new InformationModel(returningData));
                            // _infos.addAll(InformationModel.convertDataListToInfoModelList(dataList));
                            infos.setValue(_infos);
                        } catch (Exception e) {
                            H.errorLog(InformationModel.class.getSimpleName(), "getTickerHistory", "datalist'te" + new DateWrapper().getDate_AsSimpleDateFormatString() + " tarihli kayıt bulunamadı");
                        }
                    }
                });
            }

        });
        return this;
    }

    public void delete( String email, InformationModel informationModel) {
        List<InformationModel> informationModelList;
        informationModelList = infos.getValue();
        informationModelList.remove(informationModel);
        infos.setValue(informationModelList);
        /*--------------------*/
        databaseManager.deleteUserOption(email,informationModel.fromCurrency,informationModel.toCurrency);
    }
}

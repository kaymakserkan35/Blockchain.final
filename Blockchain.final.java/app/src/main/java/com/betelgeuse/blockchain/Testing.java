package com.betelgeuse.blockchain;

import android.content.Context;

import com.betelgeuse.blockchain.core.indicator.BollingerBand;
import com.betelgeuse.blockchain.core.indicator.Data;
import com.betelgeuse.blockchain.core.indicator.Period;
import com.betelgeuse.blockchain.core.indicator.RelativeStrengthIndex;
import com.betelgeuse.blockchain.core.indicator.SimpleMovingAverage;
import com.betelgeuse.blockchain.core.indicator.Sort;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;
import com.betelgeuse.blockchain.data.firebase.TickerDB;
import com.betelgeuse.blockchain.data.output.DatabaseManager;
import com.betelgeuse.blockchain.data.sqLite.UserOptionDB;
import com.betelgeuse.blockchain.userinterface.model.UserOptionModel;

import java.util.List;

public class Testing {
    Context context;
    DatabaseManager dataManager;
    public Testing (Context context) {
        this.context = context;

    }

    TickerDB tickerDB ;

    public Testing RelativeStrengthIndexTESTİNG (List<Data> dataList,Period period) {
        RelativeStrengthIndex relativeStrengthIndex = new RelativeStrengthIndex(dataList,period);
        relativeStrengthIndex.setAverage(SimpleMovingAverage.class);
        relativeStrengthIndex.analyze();
        return  this;
    }
    public Testing BollingerBandsTESTİNG (List<Data> dataList,Period period) {
        BollingerBand bollingerBand = new BollingerBand(dataList, period);
        bollingerBand.analyze();
        bollingerBand.sortDataListByCalendar(Sort.ASCENDING);
        Data data = new Data(null);
        return  this;
    }

    public Testing seedUserOptions (String email ) {
       UserOptionModel userOptionModel = new UserOptionModel(email,"BTC","USD",Period.fourteenDay,90);
        UserOptionModel userOptionModel2 = new UserOptionModel(email,"USD","TRY",Period.fourteenDay,90);
        UserOptionModel userOptionModel3 = new UserOptionModel(email,"BTC","EUR",Period.fourteenDay,90);
        DatabaseManager databaseManager = new DatabaseManager(null,new UserOptionDB(context),null);
        databaseManager.createOrUpdateUserOption(new UserOptionDTO(userOptionModel));
        databaseManager.createOrUpdateUserOption(new UserOptionDTO(userOptionModel2));
        databaseManager.createOrUpdateUserOption(new UserOptionDTO(userOptionModel3));
        return  this;
    }
    public Testing readTickerFromDateToNow(){

        return  this;
    }
    public Testing readTickerOfDay(){

        return  this;
    }
}

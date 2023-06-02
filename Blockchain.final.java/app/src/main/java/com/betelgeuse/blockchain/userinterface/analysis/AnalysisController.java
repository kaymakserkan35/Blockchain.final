package com.betelgeuse.blockchain.userinterface.analysis;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.betelgeuse.blockchain.core.indicator.BollingerBand;
import com.betelgeuse.blockchain.core.indicator.Data;
import com.betelgeuse.blockchain.core.indicator.Indicator;
import com.betelgeuse.blockchain.core.indicator.RelativeStrengthIndex;
import com.betelgeuse.blockchain.data.firebase.CurrencyDB;
import com.betelgeuse.blockchain.data.firebase.TickerDB;
import com.betelgeuse.blockchain.data.output.DatabaseManager;
import com.betelgeuse.blockchain.data.sqLite.UserOptionDB;
import com.betelgeuse.blockchain.userinterface.model.AnalysisModel;
import com.betelgeuse.blockchain.userinterface.model.modelListener.AnalysisModelListListener;

import java.util.List;

import javax.annotation.Nullable;

public class AnalysisController extends ViewModel {
    private static AnalysisController instance = null;
    public static AnalysisController getSingleton(Context context) {
        if (instance != null) return instance;
        return instance = new AnalysisController(context);
    }

    private DatabaseManager databaseManager;
    public MutableLiveData<List<AnalysisModel>> analyses = new MutableLiveData<>();

    public AnalysisController(Context context) {
        databaseManager = new DatabaseManager(new TickerDB(), new UserOptionDB(context), new CurrencyDB());
    }

    public void getData(String email, String frC, String toC, @Nullable AnalysisModelListListener listener) {
        databaseManager.readUserOption(email, frC, toC, userOption -> {
            databaseManager.getTickerHistoryLIVE(frC, toC, userOption.history+userOption.period.getCode(), tickerList -> {
                List<Data> dataList = Data.convertTickerListToDataList(tickerList);
                Indicator rsi = new RelativeStrengthIndex(dataList, userOption.period).analyze();
                Indicator bllg = new BollingerBand(dataList, userOption.period).analyze();
                List<AnalysisModel> analysisModels = null;
                analysisModels = AnalysisModel.convertDataListToAnalysisModel(dataList);
                analyses.setValue(analysisModels);
                if (listener != null) listener.onSuccess(analysisModels);
            });
        });
    }
}

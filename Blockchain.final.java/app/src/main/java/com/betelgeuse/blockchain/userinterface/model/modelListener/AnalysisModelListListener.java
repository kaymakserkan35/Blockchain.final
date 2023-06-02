package com.betelgeuse.blockchain.userinterface.model.modelListener;

import com.betelgeuse.blockchain.userinterface.model.AnalysisModel;
import com.betelgeuse.blockchain.userinterface.model.CurrencyModel;

import java.util.List;

public interface AnalysisModelListListener {
    public  void onSuccess(List<AnalysisModel> analysisModelList);
}

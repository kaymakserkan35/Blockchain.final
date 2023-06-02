package com.betelgeuse.blockchain.userinterface.model.modelListener;

import com.betelgeuse.blockchain.userinterface.model.InformationModel;

import java.util.List;

public interface InformationModelListListener {
    public  void onSuccess(List<InformationModel> infos);
}

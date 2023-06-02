package com.betelgeuse.blockchain.userinterface.model.modelListener;

import com.betelgeuse.blockchain.userinterface.model.UserOptionModel;

import java.util.List;

public interface UserOptionModelListListener {
    public  void onSuccess(List<UserOptionModel> userOptions);
}

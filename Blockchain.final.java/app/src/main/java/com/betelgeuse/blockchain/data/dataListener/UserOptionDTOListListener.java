package com.betelgeuse.blockchain.data.dataListener;

import com.betelgeuse.blockchain.data.dto.UserOptionDTO;

import java.util.List;

public interface UserOptionDTOListListener {
    void onSuccess(List<UserOptionDTO> listener);
}

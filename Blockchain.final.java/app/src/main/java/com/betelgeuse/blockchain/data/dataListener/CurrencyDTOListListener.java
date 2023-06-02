package com.betelgeuse.blockchain.data.dataListener;

import com.betelgeuse.blockchain.data.dto.CurrencyDTO;
import com.betelgeuse.blockchain.data.dto.TickerDTO;

import java.util.List;

public interface CurrencyDTOListListener {
    public  void onSuccess(List<CurrencyDTO>  currencies);
}

package com.betelgeuse.blockchain.data;

import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.UserOptionDTOListener;
import com.betelgeuse.blockchain.data.dto.UserOptionDTO;

import java.util.List;

import javax.annotation.Nullable;

public interface IUserOptionOperations {
    public boolean createOrUpdateUserOption(UserOptionDTO userOptionDTO);
    public boolean createUserOption(UserOptionDTO userOptionDTO);
    public void  readUserOption(String email, String fromCurrency, String toCurrency, UserOptionDTOListener listener);
    public List<UserOptionDTO> readUserOptionsAll (UserOptionDTOListListener listener);
    List<UserOptionDTO> readUserOptionsAllByEmail (String email,UserOptionDTOListListener listener);
    public  boolean isAny(String email,String fromCurrency,String toCurrency);
    public  boolean updateUserOption (UserOptionDTO userOptionDTO);
    public  boolean deleteUserOption(String email,String fromCurrency,String toCurrency );
}

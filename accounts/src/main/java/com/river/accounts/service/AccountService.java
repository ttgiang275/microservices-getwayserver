package com.river.accounts.service;

import com.river.accounts.dto.CustomerDto;
import jakarta.transaction.Transactional;

public interface AccountService {

    CustomerDto getAccount(String mobileNumber);

    @Transactional
    void createAccount(CustomerDto dto);

    @Transactional
    boolean updateAccount(CustomerDto dto);

    @Transactional
    boolean deleteAccount(String mobileNumber);

}

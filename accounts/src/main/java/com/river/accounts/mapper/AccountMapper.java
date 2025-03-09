package com.river.accounts.mapper;

import com.river.accounts.dto.AccountDto;
import com.river.accounts.dto.CustomerDto;
import com.river.accounts.entity.Account;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account map(AccountDto dto, Account account) {
        BeanUtils.copyProperties(dto, account);
        return account;
    }

    public AccountDto map(Account account, AccountDto dto) {
        BeanUtils.copyProperties(account, dto);
        return dto;
    }

}

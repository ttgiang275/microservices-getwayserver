package com.river.accounts.function;

import com.river.accounts.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {

    private static final Logger logger = LoggerFactory.getLogger(AccountsFunctions.class);

    @Autowired
    AccountService accountService;

    @Bean
    public Consumer<Long> updateCommunication() {
        return accountNumber -> {
            logger.info("Updating communication for account number: {}", accountNumber.toString());
            accountService.updateCommunicationStatus(accountNumber);
        };
    }

}

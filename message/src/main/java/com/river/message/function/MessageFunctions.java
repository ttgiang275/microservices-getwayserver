package com.river.message.function;

import com.river.message.dto.AccountsMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

    /**
     * Defines a function bean for sending emails.
     *
     * This function takes an AccountsMsgDto object as input, logs the email details,
     * and returns the same AccountsMsgDto object.
     *
     * The method name 'email' will be used as the name of the URL.
     *
     * @return A function that sends an email
     */
    @Bean
    public Function<AccountsMsgDto, AccountsMsgDto> email() {
        // Return a function that takes an AccountsMsgDto object as input
        return accountsMsgDto -> {
            // Log the email details for debugging purposes
            logger.info("Sending email with the details: {}", accountsMsgDto.toString());

            // Return the same AccountsMsgDto object (dummy implementation)
            return accountsMsgDto;
        };
    }

    /**
     * Defines a function bean for sending SMS messages.
     *
     * This function takes an AccountsMsgDto object as input, logs the SMS details,
     * and returns the account number of the recipient.
     *
     * The method name 'sms' will be used as the name of the URL.
     *
     * @return A function that sends an SMS message
     */
    @Bean
    public Function<AccountsMsgDto, Long> sms() {
        // Return a function that takes an AccountsMsgDto object as input
        return accountsMsgDto -> {
            // Log the SMS details for debugging purposes
            logger.info("Sending SMS with the details: {}", accountsMsgDto.toString());

            // Return the account number of the recipient (dummy implementation)
            return accountsMsgDto.accountNumber();
        };
    }

}

package com.river.accounts.service.impl;

import com.river.accounts.constants.AccountConstants;
import com.river.accounts.dto.AccountDto;
import com.river.accounts.dto.AccountsMsgDto;
import com.river.accounts.dto.CustomerDto;
import com.river.accounts.entity.Account;
import com.river.accounts.entity.Customer;
import com.river.accounts.exception.DuplicateCustomerException;
import com.river.accounts.exception.ResourceNotFoundException;
import com.river.accounts.mapper.AccountMapper;
import com.river.accounts.mapper.CustomerMapper;
import com.river.accounts.repository.AccountRepository;
import com.river.accounts.repository.CustomerRepository;
import com.river.accounts.service.AccountService;
import com.river.accounts.utils.Optionals;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerMapper customerMapper;

    private final StreamBridge streamBridge;

    @Override
    public CustomerDto getAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Account account = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = customerMapper.map(customer, new CustomerDto());
        AccountDto accountDto = accountMapper.map(account, new AccountDto());
        customerDto.setAccount(accountDto);
        return customerDto;
    }

    @Override
    public void createAccount(CustomerDto dto) {
        Customer customer = customerMapper.map(dto, new Customer());
        Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (Optionals.isPresent(existingCustomer)) {
            throw new DuplicateCustomerException(String.format("Customer already register with given mobile number %s", customer.getMobileNumber()));
        }

        Customer savedCustomer = customerRepository.save(customer);
        Account account = createNewAccount(savedCustomer);
        Account savedAccount = accountRepository.save(account);

        sendCommunication(savedAccount, savedCustomer);
    }

    @Override
    public boolean updateAccount(CustomerDto dto) {
        boolean isUpdated = false;
        AccountDto accountsDto = dto.getAccount();
        if (accountsDto != null) {
            Account account = accountRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            accountMapper.map(accountsDto, account);
            account = accountRepository.save(account);

            Integer customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            customerMapper.map(dto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Integer customerId = customer.getCustomerId();
        customerRepository.deleteById(customerId);
        accountRepository.deleteByCustomerId(customerId);
        return true;
    }

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if (accountNumber != null) {
            Account accounts = accountRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }

    private Account createNewAccount(Customer customer) {
        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        Long randomNumber = (long) (Math.random() * 1000000000L);
        account.setAccountNumber(randomNumber);
        account.setAccountType(AccountConstants.SAVINGS);
        account.setBranchAddress(AccountConstants.ADDRESS);
        return account;
    }

    private void sendCommunication(Account account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(
                account.getAccountNumber(),
                customer.getName(),
                customer.getEmail(),
                customer.getMobileNumber()
        );
        logger.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        logger.info("Is the Communication request successfully process?: {}", result);
    }

}

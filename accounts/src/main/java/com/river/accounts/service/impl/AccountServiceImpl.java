package com.river.accounts.service.impl;

import com.river.accounts.constants.AccountConstants;
import com.river.accounts.dto.AccountDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CustomerMapper customerMapper;

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
        accountRepository.save(account);
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

    private Account createNewAccount(Customer customer) {
        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        Integer randomNumber = (int) (Math.random() * 1000);
        account.setAccountNumber(randomNumber);
        account.setAccountType(AccountConstants.SAVINGS);
        account.setBranchAddress(AccountConstants.ADDRESS);
        return account;
    }

}

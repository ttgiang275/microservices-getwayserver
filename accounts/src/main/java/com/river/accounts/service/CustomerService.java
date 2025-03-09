package com.river.accounts.service;

import com.river.accounts.dto.CustomerDetailsDto;

public interface CustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);

}

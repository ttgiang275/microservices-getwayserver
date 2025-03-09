package com.river.accounts.mapper;

import com.river.accounts.dto.CustomerDetailsDto;
import com.river.accounts.dto.CustomerDto;
import com.river.accounts.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer map(CustomerDto dto, Customer customer) {
        BeanUtils.copyProperties(dto, customer);
        return customer;
    }

    public CustomerDetailsDto map(Customer customer, CustomerDetailsDto customerDetailsDto) {
        BeanUtils.copyProperties(customer, customerDetailsDto);
        return customerDetailsDto;
    }

    public CustomerDto map(Customer customer, CustomerDto dto) {
        BeanUtils.copyProperties(customer, dto);
        return dto;
    }

}

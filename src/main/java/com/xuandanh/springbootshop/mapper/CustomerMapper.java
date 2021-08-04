package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Customer;
import com.xuandanh.springbootshop.dto.CustomerDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class CustomerMapper {
    public List<CustomerDTO> customerToCustomerDTO(List<Customer> customerList) {
        return customerList.stream().filter(Objects::nonNull).map(this::customerToCustomerDTO).collect(Collectors.toList());
    }

    public CustomerDTO customerToCustomerDTO(Customer customer){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(customer,CustomerDTO.class);
    }

    public List<Customer> customerDTOToCustomer(List<CustomerDTO> customerDTOList) {
        return customerDTOList.stream().filter(Objects::nonNull).map(this::customerDTOToCustomer).collect(Collectors.toList());
    }

    public Customer customerDTOToCustomer(CustomerDTO customerDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(customerDTO , Customer.class);
    }
}

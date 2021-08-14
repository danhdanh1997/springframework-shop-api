package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Customer;
import com.xuandanh.springbootshop.dto.CustomerDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.CustomerMapper;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;
    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public CustomerMapper getCustomerMapper() {
        return customerMapper;
    }

    public CustomerService(AddressRepository addressRepository,CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressRepository = addressRepository;
    }

    public Optional<CustomerDTO>findOne(String customerId){
        return Optional.ofNullable(customerMapper
                .customerToCustomerDTO(customerRepository
                .findById(customerId).orElseThrow(()->new ResourceNotFoundException("customer with id:"+customerId+" not exist"))));
    }

    public Optional<List<CustomerDTO>>findAll(){
        return Optional.of(customerMapper
                .customerToCustomerDTO(customerRepository.findAll()));
    }

    public Optional<CustomerDTO>createCustomer(int addressId, Customer customer){
        return Optional.ofNullable(addressRepository
                .findById(addressId)
                .stream().map(address -> {
                    customer.setAddress(address);
                    return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("address with id:"+addressId+" not exist")));

    }

    public Optional<CustomerDTO>updateCustomer(Customer customer){
        return Optional.ofNullable(Optional.of(customerRepository
                .findById(customer.getCustomerId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(customer1 -> {
                    customer1.setFirstName(customer.getFirstName());
                    customer1.setLastName(customer.getLastName());
                    customer1.setEmail(customer.getEmail());
                    customer1.setActive(customer.isActive());
                    customer1.setLastUpdate(customer.getLastUpdate());
                    customer1.setCreateDate(customer.getCreateDate());
                    customer1.setPayments(customer.getPayments());
                    customer1.setStore(customer.getStore());
                    return customerMapper.customerToCustomerDTO(customerRepository.save(customer1));
                }).orElseThrow(() -> new ResourceNotFoundException("customer with id:" + customer.getCustomerId() + " not exist")));
    }
}

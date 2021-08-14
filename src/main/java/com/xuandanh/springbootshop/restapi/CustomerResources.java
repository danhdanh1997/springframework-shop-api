package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Customer;
import com.xuandanh.springbootshop.dto.AddressDTO;
import com.xuandanh.springbootshop.dto.CustomerDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.CustomerRepository;
import com.xuandanh.springbootshop.service.AddressService;
import com.xuandanh.springbootshop.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerResources {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final Logger log = LoggerFactory.getLogger(CustomerResources.class);

    public AddressService getAddressService() {
        return addressService;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public CustomerResources(AddressRepository addressRepository, AddressService addressService, CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/customer/findOne/{customerId}")
    public ResponseEntity<?>getOne(@PathVariable("customerId")String customerId){
        Optional<CustomerDTO>customerDTO = customerService.findOne(customerId);
        Map<String,Boolean>response = new HashMap<>();
        if (customerDTO.isEmpty()){
            log.error("customer with id:"+customerId+" not exist");
            response.put("customer with id:"+customerId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Information of customer with id:"+customerId+":"+customerDTO);
        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping("/customer/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<CustomerDTO>>customerDTOList = customerService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        if (customerDTOList.isEmpty()){
            log.error("List customer is empty");
            response.put("List customer is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list customer:"+customerDTOList);
        return ResponseEntity.ok(customerDTOList);
    }

    @PostMapping("/address/{addressId}/customer")
    public ResponseEntity<?>create(@PathVariable("addressId")int addressId, @RequestBody Customer customer){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist");
            response.put("address with id:"+addressId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("created customer successfully");
        return ResponseEntity.ok(customerService.createCustomer(addressId,customer));
    }

    @PutMapping("/address/{addressId}/customer/{customerId}")
    public ResponseEntity<?>update(@PathVariable("addressId")int addressId,
                                   @PathVariable("customerId")String customerId,
                                   @Valid @RequestBody Customer customer){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist");
            response.put("address with id:"+addressId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update information of customer successfully");
        return ResponseEntity.ok(customerService.updateCustomer(customer));
    }

    @DeleteMapping("/address/{addressId}/customer/{customerId}")
    public ResponseEntity<?>delete(@PathVariable("addressId")int addressId,@PathVariable("customerId")String customerId){
        Map<String,Boolean>response = new HashMap<>();
        return customerRepository.findById(customerId)
                .map(customer -> {
                    if (addressService.findOne(addressId).isPresent()){
                        log.info("delete customer successfully");
                        response.put("customer with id:"+customerId+" was deleted successfully",Boolean.TRUE);
                        customerRepository.delete(customer);
                        return ResponseEntity.ok(response);
                    }
                    response.put("address with id:"+addressId+" not exist"+" && "+ "delete customer is faild",Boolean.FALSE);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("address with id:"+addressId+" not exist"+" && "+ "delete customer is faild"));
    }
}

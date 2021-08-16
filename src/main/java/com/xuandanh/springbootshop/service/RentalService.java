package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Rental;
import com.xuandanh.springbootshop.dto.RentalDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.RentalMapper;
import com.xuandanh.springbootshop.repository.CustomerRepository;
import com.xuandanh.springbootshop.repository.InventoryRepository;
import com.xuandanh.springbootshop.repository.RentalRepository;
import com.xuandanh.springbootshop.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    public RentalRepository getRentalRepository() {
        return rentalRepository;
    }

    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public StaffRepository getStaffRepository() {
        return staffRepository;
    }

    public RentalMapper getRentalMapper() {
        return rentalMapper;
    }

    public RentalService(RentalRepository rentalRepository, RentalMapper rentalMapper, InventoryRepository inventoryRepository, CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    public Optional<RentalDTO>findOne(String rentalId){
        return Optional.ofNullable(rentalMapper
                .rentalToRentalDTO(rentalRepository
                .findById(rentalId).orElseThrow(()->new ResourceNotFoundException("rental with id:"+rentalId+" not exist"))));
    }

    public Optional<List<RentalDTO>>findAll(){
        return Optional.ofNullable(rentalMapper
                .rentalToRentalDTO(rentalRepository.findAll()));
    }

    public Optional<RentalDTO>createRental(int inventoriesId, String customerId, String staffId, Rental rental){
        return Optional.ofNullable(inventoryRepository
                .findById(inventoriesId)
                .stream().map(inventory -> {
                    rental.setInventory(inventory);
                    customerRepository.findById(customerId)
                            .stream().map(customer -> {
                                rental.setCustomer(customer);
                                staffRepository.findById(staffId)
                                        .stream().map(staff -> {
                                            rental.setStaff(staff);
                                            return rentalMapper.rentalToRentalDTO(rentalRepository.save(rental));
                                }).findAny().orElseThrow(()->new ResourceNotFoundException("staff with id:"+staffId+" not exist"));
                                return rentalMapper.rentalToRentalDTO(rentalRepository.save(rental));
                    }).findAny().orElseThrow(()->new ResourceNotFoundException("customer with id:"+customerId+" not exist"));
                    return rentalMapper.rentalToRentalDTO(rentalRepository.save(rental));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("inventory with id:"+inventoriesId+" not exist")));
    }

    public Optional<RentalDTO>updateRental(Rental rental){
        return Optional.ofNullable(Optional.of(rentalRepository
                .findById(rental.getRentalId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(rental1 -> {
                    rental1.setInventory(rental.getInventory());
                    rental1.setCustomer(rental.getCustomer());
                    rental1.setCreateDate(rental.getCreateDate());
                    rental1.setReturnDate(rental.getReturnDate());
                    rental1.setStaff(rental.getStaff());
                    rental1.setPayments(rental.getPayments());
                    rental1.setLastUpdate(rental.getLastUpdate());
                    return rentalMapper.rentalToRentalDTO(rentalRepository.save(rental1));
                }).orElseThrow(() -> new ResourceNotFoundException("rental with id:" + rental.getRentalId() + " not exist")));
    }
}

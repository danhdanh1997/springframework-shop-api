package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Staff;
import com.xuandanh.springbootshop.dto.StaffDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.StaffMapper;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.StaffRepository;
import com.xuandanh.springbootshop.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final AddressRepository addressRepository;
    private final StoreRepository storeRepository;
    public StaffRepository getStaffRepository() {
        return staffRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public StaffMapper getStaffMapper() {
        return staffMapper;
    }

    public StaffService(StoreRepository storeRepository,AddressRepository addressRepository,StaffRepository staffRepository, StaffMapper staffMapper) {
        this.staffRepository = staffRepository;
        this.staffMapper = staffMapper;
        this.addressRepository = addressRepository;
        this.storeRepository = storeRepository;
    }

    public Optional<StaffDTO>findOne(String staffId){
        return Optional.ofNullable(staffMapper
                .staffToStaffDTO(staffRepository
                .findById(staffId)
                .orElseThrow(()->new ResourceNotFoundException("Staff with id:"+staffId+" not exist"))));
    }

    public Optional<List<StaffDTO>>findAll(){
        return Optional.of(staffMapper
                .staffToStaffDTO(staffRepository.findAll()));
    }

    public Optional<StaffDTO>createStaff(int addressId, int storeId, Staff staff){
        return Optional.ofNullable(addressRepository
                .findById(addressId)
                .stream()
                .map(address -> {
                    staff.setAddress(address);
                    storeRepository.findById(storeId)
                            .stream().map(store -> {
                            staff.setStore(store);
                            return staffMapper.staffToStaffDTO(staffRepository.save(staff));
                    }).findAny().orElseThrow(()->new ResourceNotFoundException("store with id:"+storeId+" not exist"));
                    return staffMapper.staffToStaffDTO(staffRepository.save(staff));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("address with id:"+addressId+" not exist")));
    }

    public Optional<StaffDTO>updateStaff(Staff staff) {
        return Optional.ofNullable(Optional.of(staffRepository
                .findById(staff.getStaffId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(staff1 -> {
                    staff1.setFirstName(staff.getFirstName());
                    staff1.setLastName(staff.getLastName());
                    staff1.setAddress(staff.getAddress());
                    staff1.setActive(staff.isActive());
                    staff1.setEmail(staff.getEmail());
                    staff1.setImageUrl(staff.getImageUrl());
                    staff1.setLastUpdate(staff.getLastUpdate());
                    staff1.setPassword(staff.getPassword());
                    staff1.setPayments(staff.getPayments());
                    staff1.setUsername(staff.getUsername());
                    staff1.setRentals(staff.getRentals());
                    return staffMapper.staffToStaffDTO(staffRepository.save(staff1));
                }).orElseThrow(() -> new ResourceNotFoundException("staff with id:" + staff.getStaffId() + " not exist")));
    }
}

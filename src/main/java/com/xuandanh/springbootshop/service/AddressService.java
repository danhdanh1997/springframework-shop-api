package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Address;
import com.xuandanh.springbootshop.dto.AddressDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.AddressMapper;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CityRepository cityRepository;

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public AddressMapper getAddressMapper() {
        return addressMapper;
    }

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper,CityRepository cityRepository){
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
    }

    public Optional<AddressDTO>findOne(int addressId){
        return Optional.ofNullable(addressMapper
                .addressToAddressDTO(addressRepository
                .findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("address with id:"+addressId+" not exist"))));
    }

    public List<AddressDTO>findAll(){
        return Optional.of(addressMapper
                .addressToAddressDTOs(addressRepository
                .findAll())).orElseThrow(()->new ResourceNotFoundException("list not exist"));
    }

    public Optional<AddressDTO>createAddress(int citiesId, Address address){
        return Optional.of(cityRepository
                .findById(citiesId)
                .stream().map(city -> {
                    address.setCity(city);
                    return addressMapper.addressToAddressDTO(addressRepository.save(address));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("City with id:"+citiesId+" not exist")));
    }

    public Optional<AddressDTO>updateAddress(Address address){
        return Optional.of(addressRepository
                .findById(address.getAddressId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(address1 -> {
                    address1.setAddressName(address.getAddressName());
                    address1.setCity(address.getCity());
                    address1.setLastUpdate(address.getLastUpdate());
                    address1.setDistrict(address.getDistrict());
                    return addressMapper.addressToAddressDTO(addressRepository.save(address1));
                });
    }


}

package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Store;
import com.xuandanh.springbootshop.dto.StoreDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.StoreMapper;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final AddressRepository addressRepository;

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public StoreMapper getStoreMapper() {
        return storeMapper;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }


    public StoreService(StoreRepository storeRepository,  StoreMapper storeMapper,AddressRepository addressRepository){
        this.storeMapper = storeMapper;
        this.storeRepository = storeRepository;
        this.addressRepository = addressRepository;
    }

    public Optional<StoreDTO>findOne(int storeId){
        return Optional.ofNullable(storeMapper
                .storeToStoreDTO(storeRepository
                .findById(storeId)
                .orElseThrow(()-> new ResourceNotFoundException("store with id:"+storeId+" not exist"))));
    }

    public Optional<List<StoreDTO>>findAll(){
        return Optional.ofNullable(Optional.of(storeMapper
                .storeToStoreDTO(storeRepository
                        .findAll()))
                .orElseThrow(() -> new ResourceNotFoundException("List store not exist")));
    }

    public Optional<StoreDTO>createStore(int addressId, Store store){
        return Optional.of(addressRepository
                .findById(addressId)
                .stream().map(address -> {
                    store.setAddress(address);
                    return storeMapper.storeToStoreDTO(storeRepository.save(store));
                }).findAny().orElseThrow(()->new ResourceNotFoundException("address with id:"+addressId+" not exist so create store faild")));
    }

    public Optional<StoreDTO>updateStore(Store store){
        return Optional.of(storeRepository
                .findById(store.getStoreId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(store1 -> {
                   store1.setStoreName(store.getStoreName());
                   store1.setLastUpdate(store.getLastUpdate());
                   store1.setAddress(store.getAddress());
                   return storeMapper.storeToStoreDTO(storeRepository.save(store1));
                });
    }
}

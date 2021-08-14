package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Store;
import com.xuandanh.springbootshop.dto.AddressDTO;
import com.xuandanh.springbootshop.dto.StoreDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.repository.StoreRepository;
import com.xuandanh.springbootshop.service.AddressService;
import com.xuandanh.springbootshop.service.StoreService;
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
public class StoreResources {
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final AddressService addressService;
    private final Logger log = LoggerFactory.getLogger(StoreResources.class);

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public StoreService getStoreService() {
        return storeService;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public StoreResources(StoreService storeService, AddressRepository addressRepository,AddressService addressService,StoreRepository storeRepository) {
        this.storeService = storeService;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/store/findOne/{storeId}")
    public ResponseEntity<?>getOne(@PathVariable("storeId")int storeId){
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Map<String,Boolean>response = new HashMap<>();
        if (storeDTO.isEmpty()){
            log.error("store with id:"+storeId+" not exist");
            response.put("store with id:"+storeId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Information of store with id:"+storeId+" is "+storeDTO);
        return ResponseEntity.ok(storeDTO);
    }

    @GetMapping("/store/findAll")
    public ResponseEntity<?>getAll(){
        Optional<List<StoreDTO>>storeDTOList = storeService.findAll();
        Map<String,Boolean>response = new HashMap<>();
        if (storeDTOList.isEmpty()){
            log.error("List store is empty");
            response.put("List store is empty",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("Show list information of store");
        return ResponseEntity.ok(storeDTOList);
    }

    @PostMapping("/address/{addressId}/store")
    public ResponseEntity<?>create(@PathVariable("addressId")int addressId, @Valid @RequestBody Store store){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist");
            response.put("address with id:"+addressId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("create store successfully");
        return ResponseEntity.ok(storeService.createStore(addressId,store));
    }

    @PutMapping("/address/{addressId}/store/{storeId}")
    public ResponseEntity<?>update(@PathVariable("addressId")int addressId,
                                    @PathVariable("storeId")int storeId,
                                    @Valid @RequestBody Store store){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Optional<StoreDTO>storeDTO = storeService.findOne(storeId);
        Map<String,Boolean>response = new HashMap<>();
        if (addressDTO.isEmpty() && storeDTO.isEmpty()){
            log.error("address with id:"+addressId+" not exist" +" && "+ "store with id:"+storeId+ " not exist");
            response.put("address with id:"+addressId+" not exist" +" && "+ "store with id:"+storeId+ " not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update information of store successfully");
        return ResponseEntity.ok(storeService.updateStore(store));
    }

    @DeleteMapping("/address/{addressId}/store/{storeId}")
    public ResponseEntity<?>delete(@PathVariable("addressId")int addressId, @PathVariable("storeId")int storeId){
        Map<String,Boolean>response = new HashMap<>();
        return storeRepository.findById(storeId)
                .map(store -> {
                    if (addressService.findOne(addressId).isPresent()){
                        log.info("deleted store with id:"+storeId+" successfully");
                        response.put("deleted store with id:"+storeId+" successfully",Boolean.TRUE);
                        storeRepository.delete(store);
                        return ResponseEntity.ok(response);
                    }
                    log.error("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist");
                    response.put("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist",Boolean.FALSE);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("address with id:"+addressId+" not exist"+" && "+"store with id:"+storeId+" not exist"));
    }
}

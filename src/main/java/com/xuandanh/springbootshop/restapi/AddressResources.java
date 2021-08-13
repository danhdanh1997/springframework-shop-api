package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Address;
import com.xuandanh.springbootshop.dto.AddressDTO;
import com.xuandanh.springbootshop.dto.CityDTO;
import com.xuandanh.springbootshop.repository.AddressRepository;
import com.xuandanh.springbootshop.service.AddressService;
import com.xuandanh.springbootshop.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AddressResources {
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final CityService cityService;
    private final Logger log = LoggerFactory.getLogger(AddressResources.class);

    public CityService getCityService() {
        return cityService;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public AddressResources(AddressService addressService, AddressRepository addressRepository,CityService cityService){
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.cityService = cityService;
    }

    @GetMapping("/address/findOne/{addressId}")
    public ResponseEntity<?>getOne(@PathVariable("addressId")int addressId){
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Map<String,Boolean>response = new HashMap<>();

        if(addressDTO.isPresent()){
            log.info("Address with id:"+addressId+" was founded");
            response.put("Address with id:"+addressId+" was founded",Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        response.put("Address with id:"+addressId+" not exist",Boolean.FALSE);
        log.error("Address with id:"+addressId+" not exist");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/address/findAll")
    public ResponseEntity<?>getAll(){
        return ResponseEntity.ok(addressService.findAll());
    }

    @PostMapping("/city/{citiesId}/address")
    public ResponseEntity<?>create(@Valid @RequestBody Address address,@PathVariable("citiesId")int citiesId){
        Optional<CityDTO>city = cityService.findOne(citiesId);
        Map<String,Boolean>response = new HashMap<>();
        if (city.isPresent()){
            log.info("created add successfully");
            response.put("created add successfully"+ addressService.createAddress(citiesId,address),Boolean.TRUE);
            return ResponseEntity.ok(response);
        }
        log.error("city with id:"+citiesId+" not exist");
        response.put("city with id:"+citiesId+" not exist",Boolean.FALSE);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/city/{citiesId}/address/{addressId}")
    public ResponseEntity<?>update(@PathVariable("citiesId")int citiesId,
                                   @PathVariable("addressId")int addressId,
                                   @Valid @RequestBody Address address){
        Optional<CityDTO>cityDTO = cityService.findOne(citiesId);
        Optional<AddressDTO>addressDTO = addressService.findOne(addressId);
        Map<String,Boolean>response = new HashMap<>();
        if (cityDTO.isEmpty() && addressDTO.isEmpty()){
            log.error("city with id:"+citiesId+" not exist" +" && "+" address with id:"+addressId+" not exist");
            response.put("city with id:"+citiesId+" not exist" +" && "+" address with id:"+addressId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        log.info("update address successfully");
        return ResponseEntity.ok(addressService.updateAddress(address));
    }
}

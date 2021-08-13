package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.Country;
import com.xuandanh.springbootshop.dto.CountryDTO;
import com.xuandanh.springbootshop.exception.MyResourceNotFoundException;
import com.xuandanh.springbootshop.service.CountryService;
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
public class CountryResources {
    private final CountryService countryService;
    private final Logger log = LoggerFactory.getLogger(CountryResources.class);
    public CountryService getCountryService() {
        return countryService;
    }

    public CountryResources(CountryService countryService){
        this.countryService = countryService;
    }

    @GetMapping("/country/findOne/{countriesId}")
    public ResponseEntity<?> getOne(@PathVariable("countriesId")int countriesId){
        Optional<CountryDTO>countryDTO = countryService.findOne(countriesId);
        try {
            if(countryDTO.isPresent()){
                log.info("country with id:"+countriesId+" was founded");
                return ResponseEntity.ok(countryDTO);
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (MyResourceNotFoundException e){
            log.error("country with id:"+countriesId+" not exist in database");
            throw new MyResourceNotFoundException("country with id:"+countriesId+" not exist in database");
        }
    }

    @GetMapping("/country/findAll")
    public ResponseEntity<?> getAll(){
        Optional<List<CountryDTO>>countryDTOList = countryService.findAll();
        try {
            if(countryDTOList.isPresent()){
                log.info("show list country:"+countryDTOList);
                return ResponseEntity.ok(countryDTOList);
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (MyResourceNotFoundException e){
            log.error("List country not exist");
            throw new MyResourceNotFoundException("List country not exist");
        }
    }

    @PostMapping("/country/createCountry")
    public ResponseEntity<?>create(@Valid @RequestBody Country country){
        return ResponseEntity.ok(countryService.createCountry(country));
    }

    @PutMapping("/country/updateCountry/{countriesId}")
    public ResponseEntity<?> update(@PathVariable("countriesId")int countriesId,@Valid @RequestBody Country country){
        Optional<CountryDTO>countryDTO = countryService.findOne(countriesId);
        try {
            if(countryDTO.isPresent()){
                log.info("update information of country with id:"+countriesId+" successfully");
                return ResponseEntity.ok(countryService.updateCountry(country));
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (MyResourceNotFoundException e){
            log.error("country with id:"+countriesId+" not exist");
            throw new MyResourceNotFoundException("country with id:"+countriesId+"  not exist");
        }
    }

    @DeleteMapping("/country/deleteCountry/{countriesId}")
    public ResponseEntity<?> delete(@PathVariable("countriesId") int countriesId){
        Optional<CountryDTO>countryDTO = countryService.findOne(countriesId);
        Map<String,Boolean>response = new HashMap<>();
        try {
            if(countryDTO.isPresent()){
                log.info("deleted country with id:"+countriesId+" successfully");
                response.put("deleted country with id:"+countriesId+" successfully",Boolean.TRUE);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (MyResourceNotFoundException e){
            log.error("country with id:"+countriesId+" not exist");
            throw new MyResourceNotFoundException("country with id:"+countriesId+" not exist");
        }
    }
}

package com.xuandanh.springbootshop.restapi;

import com.xuandanh.springbootshop.domain.City;
import com.xuandanh.springbootshop.dto.CityDTO;
import com.xuandanh.springbootshop.dto.CountryDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.repository.CityRepository;
import com.xuandanh.springbootshop.service.CityService;
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
public class CityResources {
    private final CityService cityService;
    private final CountryService countryService;
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(CityResources.class);
    public CityService getCityService() {
        return cityService;
    }

    public CountryService getCountryService() {
        return countryService;
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public CityResources(CityService cityService, CountryService countryService, CityRepository cityRepository){
        this.cityService = cityService;
        this.countryService = countryService;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/city/findOne/{citiesId}")
    public ResponseEntity<?> getOne(@PathVariable("citiesId")int citiesId){
        Optional<CityDTO>cityDTO = cityService.findOne(citiesId);
        try{
            if(cityDTO.isPresent()){
                log.info("city with id:"+citiesId+" was founded");
                return ResponseEntity.ok(cityDTO);
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (Exception e){
            log.error("city with id:"+citiesId+" not exist in database");
            throw new ResourceNotFoundException("city with id:"+citiesId+" not exist in database");
        }
    }

    @GetMapping("/city/findAll")
    public ResponseEntity<?> findAll(){
        log.debug("test api findAll");
        Optional<List<CityDTO>>cityDTOList = cityService.findAll();
        try{
            if(cityDTOList.isPresent()){
                log.info("show list city"+cityDTOList);
                return ResponseEntity.ok(cityDTOList);
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (Exception e){
            log.error("list city not exist");
            throw new ResourceNotFoundException("list city not exist");
        }
    }

    @PostMapping("/country/{countriesId}/city")
    public ResponseEntity<?>create(@PathVariable("countriesId")int countriesId, @Valid @RequestBody City city){
        Optional<CountryDTO>country = countryService.findOne(countriesId);
        Map<String,Boolean>response = new HashMap<>();
        try {
            if(country.isPresent()){
                log.info("created city successfully"+city);
                response.put("created city successfully"+city,Boolean.TRUE);
                return ResponseEntity.ok(cityService.createCity(city,countriesId));
            }
            response.put("not found country to create city",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error("not found country to create city");
            throw new ResourceNotFoundException("not found country to create city");
        }
    }

    @PutMapping("/country/{countriesId}/city/{citiesId}")
    public ResponseEntity<?>update(@PathVariable("countriesId")int countriesId , @PathVariable("citiesId")int citiesId,@Valid @RequestBody City city){
        Optional<CountryDTO>country = countryService.findOne(countriesId);
        Map<String,Boolean>response = new HashMap<>();
        if (country.isEmpty()){
            log.error("country with id:"+countriesId+" not exist");
            response.put("country with id:"+countriesId+" not exist",Boolean.FALSE);
            return ResponseEntity.ok(response);
        }
        Optional<CityDTO>cityDTO = cityService.findOne(citiesId);
        try{
            if (cityDTO.isPresent()){
                log.info("update city with id:"+citiesId+" successfully");
                return ResponseEntity.ok(cityService.updateCity(city));
            }
            return ResponseEntity.ok(Optional.empty());
        }catch (Exception e){
            log.error("city with id:"+citiesId+" not exist");
            throw new ResourceNotFoundException("city with id:"+citiesId+" not exist");
        }
    }

    @DeleteMapping("/country/{countriesId}/city/{citiesId}")
    public ResponseEntity<?>delete(@PathVariable("countriesId")int countriesId, @PathVariable("citiesId")int citiesId){
        Map<String,Boolean>response = new HashMap<>();
        return cityRepository.findById(citiesId)
                .map(city -> {
                    if (countryService.findOne(countriesId).isPresent()){
                        cityRepository.delete(city);
                        response.put("deleted successfully",Boolean.TRUE);
                        return ResponseEntity.ok(response);
                    }
                    response.put("country with id:"+countriesId+" not exist",Boolean.FALSE);
                    return ResponseEntity.ok(response);
                }).orElseThrow(()->new ResourceNotFoundException("country with id:"+countriesId+" not exist"));
    }
}

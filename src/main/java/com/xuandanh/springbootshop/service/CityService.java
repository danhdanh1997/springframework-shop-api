package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.City;
import com.xuandanh.springbootshop.dto.CityDTO;
import com.xuandanh.springbootshop.exception.ResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.CityMapper;
import com.xuandanh.springbootshop.mapper.CountryMapper;
import com.xuandanh.springbootshop.repository.CityRepository;
import com.xuandanh.springbootshop.repository.CountryRepository;
import com.xuandanh.springbootshop.restapi.CityResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;
    private final Logger log = LoggerFactory.getLogger(CityResources.class);
    public CountryMapper getCountryMapper() {
        return countryMapper;
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public CityMapper getCityMapper() {
        return cityMapper;
    }

    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public CityService(CityRepository cityRepository, CityMapper cityMapper, CountryRepository countryRepository,CountryMapper countryMapper){
        this.cityMapper = cityMapper;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public Optional<CityDTO>findOne(Integer citiesId){
        return Optional.ofNullable(cityMapper.cityToCityDTO(cityRepository.findById(citiesId).orElseThrow(()->new ResourceNotFoundException("city with id:"+citiesId+" not exist"))));
    }

    public Optional<List<CityDTO>> findAll(){
        return Optional.ofNullable(cityMapper.cityToCityDTO(cityRepository.findAll()));
    }

    public Optional<CityDTO>createCity(City city, int countriesId){
       return Optional.of(countryRepository.findById(countriesId)
            .map(country -> {
                city.setCountry(country);
                return cityMapper.cityToCityDTO(cityRepository.save(city));
            }).orElseThrow(()->new ResourceNotFoundException("not found country")));
    }

    public Optional<CityDTO> updateCity(City city){
        return Optional.of(cityRepository
                    .findById(city
                    .getCitiesId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(city1 -> {
                        city1.setCitiesId(city.getCitiesId());
                        city1.setCitiesName(city.getCitiesName());
                        city1.setAddresses(city.getAddresses());
                        city1.setLastUpdate(city.getLastUpdate());
                        return cityMapper.cityToCityDTO(cityRepository.save(city1));
                    });
    }

}

package com.xuandanh.springbootshop.service;

import com.xuandanh.springbootshop.domain.Country;
import com.xuandanh.springbootshop.dto.CountryDTO;
import com.xuandanh.springbootshop.exception.MyResourceNotFoundException;
import com.xuandanh.springbootshop.mapper.CountryMapper;
import com.xuandanh.springbootshop.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final Logger log = LoggerFactory.getLogger(CountryService.class);
    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public CountryMapper getCountryMapper() {
        return countryMapper;
    }

    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper){
        this.countryMapper = countryMapper;
        this.countryRepository = countryRepository;
    }

    public Optional<CountryDTO> findOne(int countriesId){
        return Optional.ofNullable(countryMapper
                    .countryToCountryDTO(countryRepository
                    .findById(countriesId)
                    .orElseThrow(MyResourceNotFoundException::new)));
    }

    public Optional<List<CountryDTO>> findAll(){
        return Optional.ofNullable(countryMapper
                    .countryToCountryDTO(countryRepository
                    .findAll()));
    }

    public Optional<CountryDTO> createCountry(Country country){
        return Optional.ofNullable(countryMapper.countryToCountryDTO(countryRepository.save(country)));
    }

    public Optional<CountryDTO> updateCountry(Country country){
        return Optional.of(countryRepository
                    .findById(country.getCountriesId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(country1 -> {
                        country1.setCountriesId(country.getCountriesId());
                        country1.setCountriesName(country.getCountriesName());
                        country1.setLastUpdate(country.getLastUpdate());
                        return countryMapper.countryToCountryDTO(countryRepository.save(country1));
                    });
    }

    public void deleteCountry(CountryDTO countryDTO){
        countryRepository.findById(countryDTO.getCountriesId())
                        .ifPresent(country -> {
                            countryRepository.delete(country);
                            log.info("country with id:"+countryDTO.getCountriesId()+" was deleted successfully");
                        });
    }
}

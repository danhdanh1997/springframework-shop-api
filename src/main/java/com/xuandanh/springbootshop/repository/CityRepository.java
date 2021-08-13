package com.xuandanh.springbootshop.repository;

import com.xuandanh.springbootshop.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Integer> {
   // Optional<City>findByIdAndCountryId(Integer citiesId, Integer countriesId);
}

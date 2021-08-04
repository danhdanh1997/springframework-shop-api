package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Category;
import com.xuandanh.springbootshop.domain.City;
import com.xuandanh.springbootshop.dto.CategoryDTO;
import com.xuandanh.springbootshop.dto.CityDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class CityMapper {
    public List<CityDTO> cityToCityDTO(List<City> cityList) {
        return cityList.stream().filter(Objects::nonNull).map(this::cityToCityDTO).collect(Collectors.toList());
    }

    public CityDTO cityToCityDTO(City city){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(city,CityDTO.class);
    }

    public List<City> cityDTOToCity(List<CityDTO> cityDTOList) {
        return cityDTOList.stream().filter(Objects::nonNull).map(this::cityDTOToCity).collect(Collectors.toList());
    }

    public City cityDTOToCity(CityDTO cityDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(cityDTO , City.class);
    }
}

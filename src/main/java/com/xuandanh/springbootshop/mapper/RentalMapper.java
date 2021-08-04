package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Rental;
import com.xuandanh.springbootshop.dto.RentalDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class RentalMapper {
    public List<RentalDTO> rentalToRentalDTO(List<Rental> rentalList) {
        return rentalList.stream().filter(Objects::nonNull).map(this::rentalToRentalDTO).collect(Collectors.toList());
    }

    public RentalDTO rentalToRentalDTO(Rental rental){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(rental,RentalDTO.class);
    }

    public List<Rental> rentalDTOToRental(List<RentalDTO> rentalDTOList) {
        return rentalDTOList.stream().filter(Objects::nonNull).map(this::rentalDTOToRental).collect(Collectors.toList());
    }

    public Rental rentalDTOToRental(RentalDTO rentalDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(rentalDTO , Rental.class);
    }
}

package com.xuandanh.springbootshop.mapper;

import com.xuandanh.springbootshop.domain.Actor;
import com.xuandanh.springbootshop.domain.Address;
import com.xuandanh.springbootshop.dto.ActorDTO;
import com.xuandanh.springbootshop.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class AddressMapper {
    public List<AddressDTO> addressToAddressDTOs(List<Address> addressList) {
        return addressList.stream().filter(Objects::nonNull).map(this::addressToAddressDTO).collect(Collectors.toList());
    }

    public AddressDTO addressToAddressDTO(Address address){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(address,AddressDTO.class);
    }

    public List<Address> addressDTOToAddress(List<AddressDTO> actorDTOList) {
        return actorDTOList.stream().filter(Objects::nonNull).map(this::addressDTOToAddress).collect(Collectors.toList());
    }

    public Address addressDTOToAddress(AddressDTO addressDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressDTO , Address.class);
    }

}

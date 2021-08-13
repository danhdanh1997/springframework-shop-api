package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private int citiesId;
    private String citiesName;
    private Instant lastUpdate;
    private int countriesId;
    private Set<AddressDTO>addressDTOSet;
}

package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private int addressId;
    private String addressName;
    private String district;
    private Instant lastUpdate;
    private CityDTO city;
}

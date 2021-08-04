package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Country;
import lombok.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private int citiesId;
    private String countriesName;
    private Instant lastUpdate;
    private Country country;
}

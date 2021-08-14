package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {
    private int countriesId;
    private String countriesName;
    private Instant lastUpdate;
}

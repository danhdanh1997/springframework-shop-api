package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private int storeId;
    private String storeName;
    private AddressDTO address;
    private Instant lastUpdate;
}

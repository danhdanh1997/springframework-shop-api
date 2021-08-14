package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private int inventoriesId;
    private Instant lastUpdate;
    private FilmDTO film;
    private StoreDTO store;
}

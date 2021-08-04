package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Film;
import com.xuandanh.springbootshop.domain.Store;
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
    private Film film;
    private Store store;
}

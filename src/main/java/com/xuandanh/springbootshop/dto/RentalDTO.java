package com.xuandanh.springbootshop.dto;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {
    private String rentalId;
    private Instant createDate;
    private Instant returnDate;
    private Instant lastUpdate;
    private InventoryDTO inventory;
    private CustomerDTO customer;
    private StaffDTO staff;
}

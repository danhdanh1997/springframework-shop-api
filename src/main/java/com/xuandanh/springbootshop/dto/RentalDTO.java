package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Customer;
import com.xuandanh.springbootshop.domain.Inventory;
import com.xuandanh.springbootshop.domain.Staff;
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
    private Inventory inventory;
    private Customer customer;
    private Staff staff;
}

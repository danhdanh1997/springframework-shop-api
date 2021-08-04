package com.xuandanh.springbootshop.dto;

import com.xuandanh.springbootshop.domain.Customer;
import com.xuandanh.springbootshop.domain.Rental;
import com.xuandanh.springbootshop.domain.Staff;
import lombok.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private String paymentId;
    private int amount;
    private Instant paymentDate;
    private Staff staff;
    private Customer customer;
    private Rental rental;
}

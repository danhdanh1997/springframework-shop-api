package com.xuandanh.springbootshop.dto;

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
    private RentalDTO rental;
}

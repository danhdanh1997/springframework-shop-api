package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)",name = "payment_id")
    @Id
    private String paymentId;

    @Column(name = "amount")
    private int amount;

    @CreatedDate
    @Column(name = "payment_date")
    private Instant paymentDate;

    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "staff_id",referencedColumnName = "staff_id",nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customer_id",referencedColumnName = "customer_id",nullable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "rental_id",referencedColumnName = "rental_id",nullable = false)
    private Rental rental;
}

package com.xuandanh.springbootshop.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="address_id")
    private int addressId;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "district")
    private String district;

    @Column(name = "last_update")
    private Instant lastUpdate = Instant.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cities_id",referencedColumnName = "cities_id")
    private City city;

    @OneToMany(mappedBy="address",cascade=CascadeType.ALL)
    private List<Store> stores;

    @OneToMany(mappedBy="address",cascade=CascadeType.ALL)
    private List<Customer> customers;

    @OneToMany(mappedBy="address",cascade=CascadeType.ALL)
    private List<Staff>staff;
}

package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address  implements Serializable {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cities_id",referencedColumnName = "cities_id")
    private City city;


    @OneToOne(fetch = FetchType.LAZY,
            cascade={CascadeType.ALL,CascadeType.REMOVE},
            mappedBy = "address")
    private Store store;

    @Transient
    @Fetch(value= FetchMode.SELECT)
    @OneToMany(mappedBy="address", cascade={CascadeType.ALL,CascadeType.REMOVE},orphanRemoval = true)
    private List<Customer> customers;

    @OneToMany(mappedBy="address",fetch = FetchType.EAGER, orphanRemoval = true,cascade=CascadeType.REMOVE)
    private List<Staff>staff;
}

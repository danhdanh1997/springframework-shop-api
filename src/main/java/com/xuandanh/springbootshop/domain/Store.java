package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="store_id")
    private int storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "last_update")
    private Instant lastUpdate = Instant.now();


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy="store",cascade={CascadeType.ALL,CascadeType.REMOVE})
    private List<Customer> customers;

    @OneToMany(mappedBy="store",fetch = FetchType.EAGER, orphanRemoval = true,cascade=CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Staff> staff;

    @OneToMany(mappedBy="store",cascade=CascadeType.REMOVE)
    private List<Inventory> inventories;
}

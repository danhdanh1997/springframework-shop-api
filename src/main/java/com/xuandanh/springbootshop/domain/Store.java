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
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="store_id")
    private int storeId;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",referencedColumnName = "address_id")
    private Address address;

    @OneToMany(mappedBy="store",cascade=CascadeType.ALL)
    private List<Customer> customers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @OneToMany(mappedBy="store",cascade=CascadeType.ALL)
    private List<Inventory> inventories;
}

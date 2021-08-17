package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @JoinColumn(name = "address_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;

    @OneToMany(mappedBy="store",cascade={CascadeType.ALL,CascadeType.REMOVE},orphanRemoval = true)
    private List<Customer> customers;

    @OneToMany(mappedBy="store",cascade={CascadeType.REMOVE,CascadeType.ALL},orphanRemoval = true)
    private List<Staff> staff;

//    @OneToMany(mappedBy="store")
//    private List<Inventory> inventories;
}

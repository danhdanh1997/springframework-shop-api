package com.xuandanh.springbootshop.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="inventories_id")
    private int inventoriesId;

    @Column(name = "last_update")
    private Instant lastUpdate = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "film_id",referencedColumnName = "film_id",nullable = false)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "store_id",referencedColumnName = "store_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Store store;

    @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Rental rental;
}

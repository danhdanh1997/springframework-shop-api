package com.xuandanh.springbootshop.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Instant lastUpdate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "film_id",referencedColumnName = "film_id")
    private Film film;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id",referencedColumnName = "store_id")
    private Store store;
}

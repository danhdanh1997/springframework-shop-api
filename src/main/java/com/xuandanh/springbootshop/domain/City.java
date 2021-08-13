package com.xuandanh.springbootshop.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cities_id")
    private int citiesId;

    @Column(name = "cities_name")
    private String citiesName;

    @Column(name = "last_update")
    private Instant lastUpdate = Instant.now();

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "countries_id",referencedColumnName = "countries_id")
    private Country country;

    @OneToMany(mappedBy="city",cascade=CascadeType.ALL)
    private List<Address> addresses;
}

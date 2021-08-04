package com.xuandanh.springbootshop.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cities_id")
    private int citiesId;

    @Column(name = "cities_name")
    private String countriesName;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "countries_id",referencedColumnName = "countries_id")
    private Country country;

    @OneToMany(mappedBy="city",cascade=CascadeType.ALL)
    private List<Address> addresses;
}

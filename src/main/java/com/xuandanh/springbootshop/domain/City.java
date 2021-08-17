package com.xuandanh.springbootshop.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @NotNull
    @JoinColumn(name = "countries_id",referencedColumnName = "countries_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Country country;

    @OneToMany(mappedBy="city", cascade={CascadeType.ALL,CascadeType.REMOVE},orphanRemoval = true)
    private List<Address> addresses;
}

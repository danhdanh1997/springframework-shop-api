package com.xuandanh.springbootshop.domain;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category",uniqueConstraints = { @UniqueConstraint(columnNames = { "categories_name" }) })
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categories_id")
    private int categoriesId;

    @Column(name = "categories_name",unique=true)
    private String categoriesName;

    @Column(name = "last_update")
    private Instant lastUpdate  = Instant.now();

}

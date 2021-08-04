package com.xuandanh.springbootshop.domain;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "film_category")
public class Film_Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private Film film;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cate_id", referencedColumnName = "categories_id")
    private Category category;

    @Column(name = "last_update")
    private Instant lastUpdate;

}
